package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LikeRepo extends JpaRepository<Like, Long> {
    int countByPostId(Long postId);
    int countByCommentId(Long commentId);
    boolean existsByPostIdAndEmployeeId(Long postId, Long employeeId);
    void deleteByPostIdAndEmployeeId(Long postId, Long employeeId);
    boolean existsByCommentIdAndEmployeeId(Long commentId, Long employeeId);
    void deleteByCommentIdAndEmployeeId(Long commentId, Long employeeId);
    @Modifying
    @Query("delete from Like l where l.comment.id = :commentId")
    void deleteByCommentId(@Param("commentId") Long commentId);
}
