package bg.uni.fmi.theatre.dto.response;

import bg.uni.fmi.theatre.domain.Hall;
import bg.uni.fmi.theatre.domain.Performance;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.vo.PerformanceStatus;

import java.time.LocalDateTime;

public record PerformanceResponse(
        Long id,
        Show show,
        Hall hall,
        LocalDateTime startTime,
        PerformanceStatus status
) {
    public static PerformanceResponse from(Performance performance) {
        return new PerformanceResponse(
                performance.getId(),
                performance.getShow(),
                performance.getHall(),
                performance.getStartTime(),
                performance.getStatus()
        );
    }
}
