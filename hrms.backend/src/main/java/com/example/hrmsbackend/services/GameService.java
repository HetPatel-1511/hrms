package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.GameBookingRequestDTO;
import com.example.hrmsbackend.dtos.request.GameRequestDTO;
import com.example.hrmsbackend.dtos.request.GameConfigUpdateDTO;
import com.example.hrmsbackend.dtos.response.*;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

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

    public GameResponseDTO getGameById(Long gameId) {
        Game game = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game with ID: " + gameId + " doesn't exist"));
        return entityMapper.toGameResponseDTO(game);
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
        validateEmployeeBookingForDate(employee, gameSlot.getSlotDate(), gameSlot.getGame());

        Long cycleBookingCount = getEmployeeBookingCountInCycle(employee, gameSlot.getSlotDate(), gameSlot.getGame());
        
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

    public List<EmployeeSummaryDTO> getInterestedUsers(Long gameId) {
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game with ID: " + gameId + " doesn't exist"));
        Set<Employee> interestedEmployees = game.getInterestedEmployees();
        return entityMapper.toEmployeeSummaryDTOList(interestedEmployees.stream().toList());
    }

    public List<GameResponseDTO> getUserInterestedGames(Long userId) {
        Employee employee = employeeRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID: " + userId + " doesn't exist"));
        Set<Game> interestedGames = employee.getGamesInterestedIn();
        return entityMapper.toGameResponseDTOList(interestedGames.stream().toList());
    }

    public List<GameBookingResponseDTO> getBookings(Long employeeId) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID: " + employeeId + " doesn't exist"));
        
        List<GameBooking> bookings = gameBookingRepo.findEmployeeGameBookings(employee);
        
        Map<Game, List<GameBooking>> bookingsByGame = bookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getGameSlot().getGame()));
        
        List<GameBookingResponseDTO> response = new ArrayList<>();

        for (Map.Entry<Game, List<GameBooking>> entry : bookingsByGame.entrySet()) {
            Game game = entry.getKey();
            List<GameBooking> gameBookings = entry.getValue();
            response.add(createGameBookingResponse(game, gameBookings));
        }
        
        return response.stream()
                .sorted((a, b) -> a.getGame().getId().compareTo(b.getGame().getId()))
                .collect(Collectors.toList());
    }

    private GameBookingResponseDTO createGameBookingResponse(Game game, List<GameBooking> gameBookings) {
        GameBookingResponseDTO gameBookingResponse = new GameBookingResponseDTO();
        gameBookingResponse.setGame(entityMapper.toGameResponseDTO(game));

        Map<LocalDate, List<GameBooking>> bookingsByDate = gameBookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getGameSlot().getSlotDate()));

        List<BookingResponseDTO> bookingResponses = new ArrayList<>();

        for (Map.Entry<LocalDate, List<GameBooking>> dateEntry : bookingsByDate.entrySet()) {
            LocalDate slotDate = dateEntry.getKey();
            List<GameBooking> dateBookings = dateEntry.getValue();
            bookingResponses.add(createBookingResponse(slotDate, dateBookings));
        }

        gameBookingResponse.setBooking(bookingResponses.stream()
                .sorted(Comparator.comparing(BookingResponseDTO::getSlotDate).reversed())
                .collect(Collectors.toList()));
        return gameBookingResponse;
    }

    private BookingResponseDTO createBookingResponse(LocalDate slotDate, List<GameBooking> dateBookings) {
        BookingResponseDTO bookingResponse = new BookingResponseDTO();
        bookingResponse.setSlotDate(slotDate);

        List<BookingSlotTimeDTO> slotTimeDTOs = new ArrayList<>();

        for (GameBooking booking : dateBookings) {
            BookingSlotTimeDTO slotTimeDTO = new BookingSlotTimeDTO();
            GameSlot slot = booking.getGameSlot();

            slotTimeDTO.setStartTime(slot.getStartTime());
            slotTimeDTO.setEndTime(slot.getEndTime());
            slotTimeDTO.setBookingStatus(booking.getBookingStatus());
            slotTimeDTO.setSlotId(slot.getId());
            slotTimeDTO.setBookingId(booking.getId());
            slotTimeDTO.setBookedBy(entityMapper.toEmployeeSummaryDTO(booking.getBookedBy()));

            List<Employee> playedWithEmployees = booking.getEmployeeGameBookings().stream()
                    .map(egb -> egb.getEmployee())
                    .filter(emp -> !emp.getId().equals(booking.getBookedBy().getId()))
                    .collect(Collectors.toList());

            slotTimeDTO.setPlayedWith(entityMapper.toEmployeeSummaryDTOList(playedWithEmployees));
            slotTimeDTOs.add(slotTimeDTO);
        }

        bookingResponse.setSlotTimes(slotTimeDTOs.stream()
                .sorted(Comparator.comparing(BookingSlotTimeDTO::getStartTime).reversed())
                .collect(Collectors.toList()));
        return bookingResponse;
    }

    public List<GameSlotGroupedResponseDTO> getUpcomingSlotsForGame(Long gameId) {
        Game game = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game with ID: " + gameId + " doesn't exist"));
        
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        
        List<GameSlot> slots = gameSlotRepo.findUpcomingSlotsByGameId(gameId, currentDate);
        
        List<GameSlot> upcomingSlots = slots.stream()
                .filter(slot -> {
                    if (slot.getSlotDate().isEqual(currentDate)) {
                        return slot.getStartTime().isAfter(currentTime); // Current date (after current time)
                    }
                    return true; // Future dates
                })
                .collect(Collectors.toList());
        
        Map<LocalDate, List<GameSlot>> slotsByDate = upcomingSlots.stream()
                .collect(Collectors.groupingBy(GameSlot::getSlotDate));
        
        return slotsByDate.entrySet().stream()
                .map(entry -> {
                    GameSlotGroupedResponseDTO responseDTO = new GameSlotGroupedResponseDTO();
                    responseDTO.setSlotDate(entry.getKey());
                    
                    List<SlotTimeDTO> slotTimes = entry.getValue().stream()
                            .map(slot -> {
                                SlotTimeDTO slotTimeDTO = new SlotTimeDTO();
                                slotTimeDTO.setStartTime(slot.getStartTime());
                                slotTimeDTO.setEndTime(slot.getEndTime());
                                slotTimeDTO.setSlotStatus(slot.getSlotStatus());
                                slotTimeDTO.setSlotId(slot.getId());
                                return slotTimeDTO;
                            })
                            .collect(Collectors.toList());
                    responseDTO.setSlotTimes(slotTimes);
                    return responseDTO;
                })
                .sorted((a, b) -> a.getSlotDate().compareTo(b.getSlotDate()))
                .collect(Collectors.toList());
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

    private void validateEmployeeBookingForDate(Employee employee, LocalDate slotDate, Game game) {
        List<GameBooking> existingBookings = gameBookingRepo.findEmployeeBookingsForDate(employee, slotDate, game.getId());
        if (!existingBookings.isEmpty()) {
            throw new RuntimeException("Employee already has a booking for this date.");
        }
    }

    private Long getEmployeeBookingCountInCycle(Employee employee, LocalDate date, Game game) {
        LocalDate today = date;
        LocalDate cycleStartDate = today.minusDays(CYCLE_DAYS-1);
        return gameBookingRepo.countEmployeeBookingsInCycle(employee, cycleStartDate, today, game.getId());
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

        GameBooking selectedBooking = findFairBooking(waitingBookings, gameSlot.getSlotDate(), gameSlot.getGame());
        
        selectedBooking.setBookingStatus("CONFIRMED");
        gameSlot.setSlotStatus("BOOKED");
        
        gameBookingRepo.save(selectedBooking);
        gameSlotRepo.save(gameSlot);

        return "Slot allocated to " + selectedBooking.getBookedBy().getName() + " for " + gameSlot.getGame().getName() + " on " + gameSlot.getSlotDate();
    }

    private GameBooking findFairBooking(List<GameBooking> waitingBookings, LocalDate date, Game game) {
        LocalDate today = date;
        LocalDate cycleStartDate = today.minusDays(CYCLE_DAYS-1);
        
        return waitingBookings.stream()
                .min((b1, b2) -> {
                    Long count1 = gameBookingRepo.countEmployeeBookingsInCycle(b1.getBookedBy(), cycleStartDate, today, game.getId());
                    Long count2 = gameBookingRepo.countEmployeeBookingsInCycle(b2.getBookedBy(), cycleStartDate, today, game.getId());
                    
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

    @Transactional
    public String updateGameConfiguration(Long gameId, GameConfigUpdateDTO dto) {
        Game game = gameRepo.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game with ID: " + gameId + " doesn't exist"));
        
        GameConfiguration gameConfiguration = gameConfigurationRepo.findByGameId(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game configuration not found for game ID: " + gameId));
        
        gameConfiguration.setSlotReleaseTime(dto.getSlotReleaseTime());
        gameConfiguration.setSlotDurationMinutes(dto.getSlotDurationMinutes());
        gameConfiguration.setMaxPlayersPerSlot(dto.getMaxPlayersPerSlot());
        gameConfiguration.setOperatingStartTime(dto.getOperatingStartTime());
        gameConfiguration.setOperatingEndTime(dto.getOperatingEndTime());
        
        game.setActive(dto.getActive());
        
        gameConfigurationRepo.save(gameConfiguration);
        gameRepo.save(game);
        
        return "Game configuration updated successfully";
    }

    private record GameResult(Game game, GameConfiguration savedGameConfiguration) {
    }
}
