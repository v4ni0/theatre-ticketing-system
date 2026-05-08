package bg.uni.fmi.theatre.domain;

import bg.uni.fmi.theatre.validation.Validator;
import bg.uni.fmi.theatre.vo.PerformanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "performance")
@NoArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerformanceStatus status;

    @Version
    private Long version;

    public Performance(Show show, Hall hall, LocalDateTime startTime) {
        Validator.validateNotNull(show, "show is required");
        Validator.validateNotNull(hall, "hall is required");
        Validator.validateNotNull(startTime, "startTime is required");
        this.show = show;
        this.hall = hall;
        this.startTime = startTime;
        this.status = PerformanceStatus.SCHEDULED;
    }
}