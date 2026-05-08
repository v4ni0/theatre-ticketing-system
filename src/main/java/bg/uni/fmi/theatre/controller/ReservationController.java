package bg.uni.fmi.theatre.controller;

import bg.uni.fmi.theatre.dto.request.ReservationRequest;
import bg.uni.fmi.theatre.dto.response.ReservationResponse;
import bg.uni.fmi.theatre.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Seat booking operations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse bookSeat(@Valid @RequestBody ReservationRequest req) {
        return reservationService.bookSeat(req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }

    @GetMapping
    public List<ReservationResponse> listByPerformance(@RequestParam Long performanceId) {
        return reservationService.findByPerformanceId(performanceId);
    }
}