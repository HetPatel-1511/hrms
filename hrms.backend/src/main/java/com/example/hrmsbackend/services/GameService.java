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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private Integer CYCLE_DAYS = 2;

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
        Employee employee = employeeRepo.findByEmail(userDetails.getUsername());

        validateSlotAvailability(gameSlot);
        validateMaxPlayer(gameSlot.getGame(), dto.getEmployeeIds().size()+1);
        validateSlotIsInFuture(gameSlot);
        validateIsInterestedInGame(employee, gameSlot.getGame());

        // WAITING or CONFIRMED bookings for employee are not allowed
        validateEmployeeBookingForDate(employee, gameSlot.getSlotDate());

        Long cycleBookingCount = getEmployeeBookingCountInCycle(employee, gameSlot.getSlotDate());
        
        GameBooking gameBooking = new GameBooking();
        gameBooking.setBookedBy(employee);
        gameBooking.setGameSlot(gameSlot);
        
        if (cycleBookingCount == 0) {
            gameBooking.setBookingStatus("CONFIRMED");
            gameSlot.setSlotStatus("BOOKED");
        } else {
            gameBooking.setBookingStatus("WAITING");
        }
        
        GameBooking savedGameBooking = gameBookingRepo.save(gameBooking);
        saveEmployeeGameBooking(dto, savedGameBooking, employee);
        gameSlotRepo.save(gameSlot);

        if (cycleBookingCount == 0) {
            return "Booking confirmed for " + gameSlot.getGame().getName() + " on " + gameSlot.getSlotDate() + " from " + gameSlot.getStartTime() + " to " + gameSlot.getEndTime();
        } else {
            return "Added to waiting list for " + gameSlot.getGame().getName() + " on " + gameSlot.getSlotDate() + " from " + gameSlot.getStartTime() + " to " + gameSlot.getEndTime();
        }
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

    private void validateSlotAvailability(GameSlot gameSlot) {
        if (!gameSlot.getSlotStatus().equals("AVAILABLE")) {
            throw new RuntimeException("Game slot is not available for booking.");
        }

        if (!gameSlot.getGame().getActive()) {
            throw new RuntimeException("Currently the game is inactive for booking.");
        }
    }

    private void validateMaxPlayer(Game game, Integer playerCount) {
        Integer maxPlayers = game.getGameConfiguration().getMaxPlayersPerSlot();
        if (playerCount > maxPlayers) {
            throw new RuntimeException("Maximum " + maxPlayers + " players are allowed to play.");
        }
    }

    private void validateIsInterestedInGame(Employee employee, Game game) {
        if (!employee.getGamesInterestedIn().contains(game)) {
            throw new RuntimeException("Employee is not interested in this game.");
        }
    }
    
    private void validateSlotIsInFuture(GameSlot gameSlot) {
        LocalDateTime slotDateTime = LocalDateTime.of(gameSlot.getSlotDate(), gameSlot.getStartTime());
        if (slotDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Game slot date is in the past.");
        }
    }

    private void validateEmployeeBookingForDate(Employee employee, LocalDate slotDate) {
        List<GameBooking> existingBookings = gameBookingRepo.findEmployeeBookingsForDate(employee, slotDate);
        if (!existingBookings.isEmpty()) {
            throw new RuntimeException("Employee already has a booking for this date.");
        }
    }

    private Long getEmployeeBookingCountInCycle(Employee employee, LocalDate date) {
        LocalDate today = date;
        LocalDate cycleStartDate = today.minusDays(CYCLE_DAYS-1);
        return gameBookingRepo.countEmployeeBookingsInCycle(employee, cycleStartDate, today);
    }

    @Transactional
    public String allocateSlot(Long slotId) {
        GameSlot gameSlot = gameSlotRepo.findById(slotId).orElseThrow(()->new ResourceNotFoundException("Invalid Slot Id"));
        
        if (!gameSlot.getSlotStatus().equals("AVAILABLE")) {
            throw new RuntimeException("Slot is not available for allocation.");
        }

        List<GameBooking> waitingBookings = gameBookingRepo.findWaitingBookingsForSlot(slotId);
        if (waitingBookings.isEmpty()) {
            throw new RuntimeException("No waiting bookings found for this slot.");
        }

        GameBooking selectedBooking = findFairBooking(waitingBookings, gameSlot.getSlotDate());
        
        selectedBooking.setBookingStatus("CONFIRMED");
        gameSlot.setSlotStatus("BOOKED");
        
        gameBookingRepo.save(selectedBooking);
        gameSlotRepo.save(gameSlot);

        return "Slot allocated to " + selectedBooking.getBookedBy().getName() + " for " + gameSlot.getGame().getName() + " on " + gameSlot.getSlotDate();
    }

    private GameBooking findFairBooking(List<GameBooking> waitingBookings, LocalDate date) {
        LocalDate today = date;
        LocalDate cycleStartDate = today.minusDays(CYCLE_DAYS-1);
        
        return waitingBookings.stream()
                .min((b1, b2) -> {
                    Long count1 = gameBookingRepo.countEmployeeBookingsInCycle(b1.getBookedBy(), cycleStartDate, today);
                    Long count2 = gameBookingRepo.countEmployeeBookingsInCycle(b2.getBookedBy(), cycleStartDate, today);
                    
                    int countCompare = count1.compareTo(count2);
                    if (countCompare != 0) {
                        return countCompare;
                    }
                    
                    return b1.getCreatedAt().compareTo(b2.getCreatedAt());
                })
                .orElse(waitingBookings.get(0));
    }

    @Transactional
    public String cancelBooking(Long bookingId, CustomUserDetails userDetails) {
        GameBooking booking = gameBookingRepo.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        Employee employee = employeeRepo.findByEmail(userDetails.getUsername());
        
        if (!booking.getBookedBy().getId().equals(employee.getId())) {
            throw new RuntimeException("You can only cancel your own bookings.");
        }

        if (!booking.getBookingStatus().equals("CONFIRMED") && !booking.getBookingStatus().equals("WAITING")) {
            throw new RuntimeException("Only confirmed or waiting bookings can be cancelled.");
        }

        validateCancelationTime(booking.getGameSlot());

        booking.setBookingStatus("CANCELLED");
        booking.setCancelledAt(LocalDateTime.now());
        gameBookingRepo.save(booking);

        if (booking.getGameSlot().getSlotStatus().equals("BOOKED")) {
            booking.getGameSlot().setSlotStatus("AVAILABLE");
            gameSlotRepo.save(booking.getGameSlot());
            
            try {
                allocateSlot(booking.getGameSlot().getId());
            } catch (RuntimeException e) {
            }
        }

        return "Booking cancelled successfully.";
    }

    private static void validateCancelationTime(GameSlot gameSlot) {
        LocalDateTime slotStartDateTime = LocalDateTime.of(gameSlot.getSlotDate(), gameSlot.getStartTime());
        LocalDateTime cutoffTime = slotStartDateTime.minusMinutes(30);

        if (LocalDateTime.now().isAfter(cutoffTime)) {
            throw new RuntimeException("Cancellation not allowed within 30 minutes of slot start time.");
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
        gameConfiguration.setSlotReleaseTime(dto.getSlotReleaseTime());
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
