package com.example.hrmsbackend.dtos.response;

public class GameResponseDTO {
    private Long id;
    private String name;
    private Boolean isActive;
    private GameConfigResponseDTO gameConfiguration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public GameConfigResponseDTO getGameConfiguration() {
        return gameConfiguration;
    }

    public void setGameConfiguration(GameConfigResponseDTO gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }
}
