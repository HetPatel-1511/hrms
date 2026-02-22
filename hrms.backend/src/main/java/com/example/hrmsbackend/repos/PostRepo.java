package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    @Query("select p from Post p where p.id = :id and p.isDeleted = false")
    java.util.Optional<Post> findActiveById(@Param("id") Long id);

    @Query("select p from Post p where p.isDeleted = false order by p.createdAt desc")
    List<Post> findAllActiveOrderByCreatedAtDesc();
}
