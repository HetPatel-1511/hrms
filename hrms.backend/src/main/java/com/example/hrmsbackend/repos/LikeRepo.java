package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepo extends JpaRepository<Like, Long> {
    int countByPostId(Long postId);
    int countByCommentId(Long commentId);
    boolean existsByPostIdAndEmployeeId(Long postId, Long employeeId);
    void deleteByPostIdAndEmployeeId(Long postId, Long employeeId);
    boolean existsByCommentIdAndEmployeeId(Long commentId, Long employeeId);
    void deleteByCommentIdAndEmployeeId(Long commentId, Long employeeId);
}
