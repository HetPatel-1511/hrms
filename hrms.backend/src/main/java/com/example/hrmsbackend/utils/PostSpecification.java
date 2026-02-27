package com.example.hrmsbackend.utils;

import com.example.hrmsbackend.entities.Post;
import com.example.hrmsbackend.entities.PostTag;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification {
    public static Specification<Post> filterPosts(Long authorId, String tagName, LocalDate startDate,
                                                   LocalDate endDate, String searchQuery) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by author ID
            if (authorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("id"), authorId));
            }

            // Filter by tag name
            if (tagName != null && !tagName.isBlank()) {
                Subquery<Long> tagSubquery = query.subquery(Long.class);
                Root<PostTag> postTagRoot = tagSubquery.from(PostTag.class);
                tagSubquery.select(postTagRoot.get("post").get("id"))
                        .where(criteriaBuilder.equal(postTagRoot.get("tag").get("tag"), tagName));
                predicates.add(root.get("id").in(tagSubquery));
            }

            // Filter by date range
            if (startDate != null) {
                LocalDateTime startDateTime = startDate.atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDateTime));
            }

            if (endDate != null) {
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDateTime));
            }

            // Search by title, description, author name, or author email
            if (searchQuery != null && !searchQuery.isBlank()) {
                String searchPattern = "%" + searchQuery.toLowerCase() + "%";

                Predicate titlePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), searchPattern);
                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), searchPattern);
                Predicate authorNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("author").get("name")), searchPattern);
                Predicate authorEmailPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("author").get("email")), searchPattern);

                Predicate searchPredicate = criteriaBuilder.or(
                        titlePredicate, descriptionPredicate, authorNamePredicate, authorEmailPredicate);
                predicates.add(searchPredicate);
            }

            // Exclude deleted posts
            predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

            // Order by creation date descending
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            // Combine all predicates with AND logic
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
