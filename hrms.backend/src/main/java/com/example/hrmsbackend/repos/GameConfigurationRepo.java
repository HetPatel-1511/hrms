package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Game;
import com.example.hrmsbackend.entities.GameConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameConfigurationRepo extends JpaRepository<GameConfiguration, Long> {

}
