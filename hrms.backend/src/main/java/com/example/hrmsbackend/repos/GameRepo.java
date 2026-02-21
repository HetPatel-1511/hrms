package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends JpaRepository<Game, Long> {
    Boolean existsByName(String name);
}
