package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
    int countByPostId(Long postId);
}
