package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.GameBookingRequestDTO;
import com.example.hrmsbackend.dtos.request.GameRequestDTO;
import com.example.hrmsbackend.dtos.response.GameResponseDTO;
import com.example.hrmsbackend.entities.*;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.*;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameService {
    private GameRepo gameRepo;
    private GameConfigurationRepo gameConfigurationRepo;
    private GameSlotRepo gameSlotRepo;
    private EntityMapper entityMapper;
    private EmployeeRepo employeeRepo;
    private GameBookingRepo gameBookingRepo;
    private EmployeeGameBookingRepo employeeGameBookingRepo;

    @Autowired
    public GameService(GameRepo gameRepo, GameConfigurationRepo gameConfigurationRepo, GameSlotRepo gameSlotRepo, EntityMapper entityMapper, EmployeeRepo employeeRepo, GameBookingRepo gameBookingRepo, EmployeeGameBookingRepo employeeGameBookingRepo) {
        this.gameRepo = gameRepo;
        this.gameConfigurationRepo = gameConfigurationRepo;
        this.gameSlotRepo = gameSlotRepo;
        this.entityMapper = entityMapper;
        this.employeeRepo = employeeRepo;
        this.gameBookingRepo = gameBookingRepo;
        this.employeeGameBookingRepo = employeeGameBookingRepo;
    }

    @Transactional
    public String create(GameRequestDTO dto) {
        Boolean exists = gameRepo.existsByName(dto.getName());
        if (exists) {
            throw new RuntimeException("Game already exists");
        }

        GameResult result = saveGameWithConfiguration(dto);
        return "Game created successfully";
    }

    public List<GameResponseDTO> getAll() {
        List<Game> games = gameRepo.findAll();
        return entityMapper.toGameResponseDTOList(games);
    }

    @Transactional
    public String book(GameBookingRequestDTO dto, CustomUserDetails userDetails) {
        GameSlot gameSlot = gameSlotRepo.findById(dto.getGameSlotId()).orElseThrow(()->new ResourceNotFoundException("Invalid booking slot"));

        validateBooking(dto, gameSlot);

        Employee employee = employeeRepo.findByEmail(userDetails.getUsername());

        GameBooking gameBooking = new GameBooking();
        gameBooking.setBookedBy(employee);
        gameBooking.setGameSlot(gameSlot);
        gameBooking.setBookingStatus("BOOKED");
        GameBooking savedGameBooking = gameBookingRepo.save(gameBooking);

        saveEmployeeGameBooking(dto, savedGameBooking, employee);

        gameSlot.setSlotStatus("BOOKED");
        return "Booking for " + gameSlot.getGame().getName() + " on " + gameSlot.getSlotDate() + " from " + gameSlot.getStartTime() + " to " + gameSlot.getEndTime() + " is successfull.";
    }

    @Transactional
    public String interested(Long gameId, CustomUserDetails userDetails) {
        Game game = gameRepo.findById(gameId).orElseThrow(()-> new ResourceNotFoundException("Game with ID: " + gameId + " doesn't exist"));
        Employee employee = employeeRepo.findByEmail(userDetails.getUsername());
        if (employee.getGamesInterestedIn().contains(game)) {
            employee.removeGameInterestedIn(game);
            return "Interest removed successfully";
        } else {
            employee.addGameInterestedIn(game);
            return "Interest added successfully";
        }
    }

    private static void validateBooking(GameBookingRequestDTO dto, GameSlot gameSlot) {
        if (gameSlot.getSlotStatus().equals("BOOKED")) {
            throw new RuntimeException("Game slot is already booked.");
        }

        if (!gameSlot.getGame().getActive()) {
            throw new RuntimeException("Currently the game is inactive for booking.");
        }

        Integer maxPlayers = gameSlot.getGame().getGameConfiguration().getMaxPlayersPerSlot();
        if (dto.getEmployeeIds().size()+1 > maxPlayers) {
            throw new RuntimeException("Maximum " + maxPlayers + " players are allowed to play.");
        }
    }

    private void saveEmployeeGameBooking(GameBookingRequestDTO dto, GameBooking savedGameBooking, Employee employee) {
        EmployeeGameBooking employeeGameBooking = new EmployeeGameBooking();
        employeeGameBooking.setGameBooking(savedGameBooking);
        employeeGameBooking.setEmployee(employee);
        employeeGameBookingRepo.save(employeeGameBooking);

        for (Long id: dto.getEmployeeIds()) {
            Employee player = employeeRepo.findById(id).orElse(null);
            if (player == null) {
                continue;
            }
            EmployeeGameBooking employeeGameBooking1 = new EmployeeGameBooking();
            employeeGameBooking1.setGameBooking(savedGameBooking);
            employeeGameBooking1.setEmployee(player);
            employeeGameBookingRepo.save(employeeGameBooking1);
        }
    }

    private @NonNull GameResult saveGameWithConfiguration(GameRequestDTO dto) {
        Game game = new Game();
        game.setName(dto.getName());
        Game savedGame = gameRepo.save(game);

        GameConfiguration gameConfiguration = new GameConfiguration();
        gameConfiguration.setGame(savedGame);
        gameConfiguration.setDurationBeforeSlotOpens(dto.getDurationBeforeSlotOpens());
        gameConfiguration.setOperatingStartTime(dto.getOperatingStartTime());
        gameConfiguration.setOperatingEndTime(dto.getOperatingEndTime());
        gameConfiguration.setMaxPlayersPerSlot(dto.getMaxPlayersPerSlot());
        gameConfiguration.setSlotDurationMinutes(dto.getSlotDurationMinutes());
        GameConfiguration savedGameConfiguration = gameConfigurationRepo.save(gameConfiguration);
        GameResult result = new GameResult(game, savedGameConfiguration);
        return result;
    }

    private record GameResult(Game game, GameConfiguration savedGameConfiguration) {
    }
}
