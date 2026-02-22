package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {
	Tag findByTag(String tag);
}
