package bg.uni.fmi.theatre.service.integration;

import bg.uni.fmi.theatre.domain.Hall;
import bg.uni.fmi.theatre.domain.Performance;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.dto.request.ReservationRequest;
import bg.uni.fmi.theatre.dto.response.ReservationResponse;
import bg.uni.fmi.theatre.exception.ValidationException;
import bg.uni.fmi.theatre.repository.HallRepository;
import bg.uni.fmi.theatre.repository.PerformanceRepository;
import bg.uni.fmi.theatre.repository.ShowRepository;
import bg.uni.fmi.theatre.service.ReservationService;
import bg.uni.fmi.theatre.vo.AgeRating;
import bg.uni.fmi.theatre.vo.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ReservationServiceIntegrationTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired private PerformanceRepository performanceRepository;
    @Autowired private ShowRepository showRepository;
    @Autowired private HallRepository hallRepository;

    private Performance testPerformance;

    @BeforeEach
    void setUp() {
        Show show = showRepository.save(
                new Show("Test Show", "desc", Genre.COMEDY, 90, AgeRating.ALL));
        Hall hall = hallRepository.save(new Hall("Test Hall", 100));
        testPerformance = performanceRepository.save(
                new Performance(show, hall, LocalDateTime.of(2026, 9, 1, 19, 0)));
    }

    @Test
    void bookSeat_succeeds_forAvailableSeat() {
        ReservationRequest req = new ReservationRequest();
        req.setPerformanceId(testPerformance.getId());
        req.setSeatLabel("A1");
        req.setCustomerName("Alice");

        ReservationResponse response = reservationService.bookSeat(req);
        assertEquals("A1", response.seatLabel(), "");
    }

    @Test
    void bookSeat_fails_whenSeatAlreadyBooked() {
        ReservationRequest req = new ReservationRequest();
        req.setPerformanceId(testPerformance.getId());
        req.setSeatLabel("B2");
        req.setCustomerName("Alice");

        reservationService.bookSeat(req); // first booking succeeds

        req.setCustomerName("Bob");

        assertThrows(ValidationException.class, () -> reservationService.bookSeat(req),"Booking the same seat again should throw ValidationException");
    }

}
