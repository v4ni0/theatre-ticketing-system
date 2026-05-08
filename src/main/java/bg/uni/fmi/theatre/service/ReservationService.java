package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.domain.Performance;
import bg.uni.fmi.theatre.domain.Reservation;
import bg.uni.fmi.theatre.dto.request.ReservationRequest;
import bg.uni.fmi.theatre.dto.response.ReservationResponse;
import bg.uni.fmi.theatre.exception.NotFoundException;
import bg.uni.fmi.theatre.exception.ValidationException;
import bg.uni.fmi.theatre.repository.PerformanceRepository;
import bg.uni.fmi.theatre.repository.ReservationRepository;
import bg.uni.fmi.theatre.vo.PerformanceStatus;
import bg.uni.fmi.theatre.vo.ReservationStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PerformanceRepository performanceRepository;

    public ReservationService(ReservationRepository reservationRepository, PerformanceRepository performanceRepository) {
        this.reservationRepository = reservationRepository;
        this.performanceRepository = performanceRepository;
    }

    public List<ReservationResponse> findByPerformanceId(Long performanceId) {
        if (!performanceRepository.existsById(performanceId)) {
            throw new NotFoundException("Performance not found: " + performanceId);
        }
        return reservationRepository.findByPerformanceId(performanceId).stream()
                .map(ReservationResponse::from)
                .toList();
    }
    @Transactional
    public ReservationResponse bookSeat(ReservationRequest reservationRequest) {
        Performance performance = performanceRepository.findById(reservationRequest.getPerformanceId())
                .orElseThrow(() -> new NotFoundException("Performance not found: " + reservationRequest.getPerformanceId()));

        if (performance.getStatus() != PerformanceStatus.SCHEDULED) {
            throw new ValidationException("Cannot book seats for a "
                    + performance.getStatus().name().toLowerCase() + " performance");
        }

        if (reservationRepository.existsByPerformanceIdAndSeatLabel(
                reservationRequest.getPerformanceId(), reservationRequest.getSeatLabel())) {
            throw new ValidationException("Seat " + reservationRequest.getSeatLabel() + " is already booked");
        }

        Reservation reservation = new Reservation(
                performance,
                reservationRequest.getSeatLabel(),
                reservationRequest.getCustomerName());
        Reservation saved = reservationRepository.save(reservation);

        return ReservationResponse.from(saved);
    }

    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + id));
        reservation.setStatus(ReservationStatus.CANCELLED);
    }
}