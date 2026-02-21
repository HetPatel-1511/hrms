package com.example.hrmsbackend.services;

import com.example.hrmsbackend.entities.Game;
import com.example.hrmsbackend.entities.GameConfiguration;
import com.example.hrmsbackend.entities.GameSlot;
import com.example.hrmsbackend.repos.GameConfigurationRepo;
import com.example.hrmsbackend.repos.GameRepo;
import com.example.hrmsbackend.repos.GameSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class GameSlotSchedulerService {
    private final GameRepo gameRepo;
    private final GameConfigurationRepo gameConfigurationRepo;
    private final GameSlotRepo gameSlotRepo;

    @Autowired
    public GameSlotSchedulerService(GameRepo gameRepo, GameConfigurationRepo gameConfigurationRepo, GameSlotRepo gameSlotRepo) {
        this.gameRepo = gameRepo;
        this.gameConfigurationRepo = gameConfigurationRepo;
        this.gameSlotRepo = gameSlotRepo;
    }

    @Transactional
    public void generateSlotsForGame(Game game, LocalDate date) {
        GameConfiguration config = game.getGameConfiguration();
        if (config == null) {
            System.out.println("No configuration found for game: " + game.getName());
            return;
        }

        // Check if slots already exist for this game and date
        boolean slotsExist = gameSlotRepo.existsByGameIdAndSlotDate(game.getId(), date);
        if (slotsExist) {
            System.out.println("Slots already exist for game " + game.getName() + " on date " + date);
            return;
        }

        LocalTime startTime = config.getOperatingStartTime();
        LocalTime endTime = config.getOperatingEndTime();
        Integer slotDurationMinutes = config.getSlotDurationMinutes();

        if (startTime == null || endTime == null || slotDurationMinutes == null) {
            System.out.println("Invalid configuration for game: " + game.getName() +
                        ". Missing startTime, endTime, or slotDurationMinutes");
            return;
        }

        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            System.out.println("Invalid operating hours for game: " + game.getName() +
                        ". Start time must be before end time");
            return;
        }

        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            LocalTime slotEndTime = currentTime.plusMinutes(slotDurationMinutes);

            if (slotEndTime.isAfter(endTime)) {
                break;
            }

            GameSlot slot = new GameSlot();
            slot.setGame(game);
            slot.setSlotDate(date);
            slot.setStartTime(currentTime);
            slot.setEndTime(slotEndTime);
            slot.setSlotStatus("AVAILABLE");

            gameSlotRepo.save(slot);

            System.out.println("Created slot for game " + game.getName() + " on " + date + " from " + currentTime + " to " + slotEndTime);

            currentTime = slotEndTime;
        }
    }

    @Scheduled(fixedDelay = 600000) // checks every 10 minutes
    @Transactional
    public void checkAndGenerateSlotsBasedOnReleaseTime() {
        System.out.println("Checking for slot generation based on release time");
        LocalTime currentTime = LocalTime.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<GameConfiguration> configs = gameConfigurationRepo.findAll();

        for (GameConfiguration config : configs) {
            if (config.getSlotReleaseTime() == null || !config.getGame().getActive()) {
                continue;
            }

            LocalTime releaseTime = config.getSlotReleaseTime();

            if (gameSlotRepo.existsByGameIdAndSlotDate(config.getGame().getId(), tomorrow)) {
                System.out.println("Slots already generated for game: " + config.getGame().getName());
                continue;
            }

            if (currentTime.equals(releaseTime) || currentTime.isAfter(releaseTime)) {
                System.out.println("Triggering slot generation for game: " + config.getGame().getName() + " at release time: " + releaseTime);
                generateSlotsForGame(config.getGame(), tomorrow);
            }
        }
    }
}
