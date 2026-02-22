package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepo extends JpaRepository<PostTag, Long> {
	@org.springframework.data.jpa.repository.Modifying
	@org.springframework.data.jpa.repository.Query("delete from PostTag pt where pt.post.id = :postId")
	void deleteByPostId(@org.springframework.data.repository.query.Param("postId") Long postId);
}
