package bg.uni.fmi.theatre.dto.response;

import bg.uni.fmi.theatre.domain.Reservation;
import bg.uni.fmi.theatre.vo.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long performanceId,
        String seatLabel,
        String customerName,
        ReservationStatus status,
        LocalDateTime reservedAt
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getPerformance().getId(),
                reservation.getSeatLabel(),
                reservation.getCustomerName(),
                reservation.getStatus(),
                reservation.getReservedAt()
        );
    }
}