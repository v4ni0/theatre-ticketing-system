package bg.uni.fmi.theatre.domain;

import bg.uni.fmi.theatre.vo.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @Column(name = "seat_label", nullable = false, length = 20)
    private String seatLabel;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    @Version
    private Long version;

    protected Reservation() {}

    public Reservation(Performance performance, String seatLabel, String customerName) {
        this.performance = performance;
        this.seatLabel = seatLabel;
        this.customerName = customerName;
        this.status = ReservationStatus.CONFIRMED;
        this.reservedAt = LocalDateTime.now();
    }
}