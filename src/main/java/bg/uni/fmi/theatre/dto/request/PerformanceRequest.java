package bg.uni.fmi.theatre.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PerformanceRequest {
    @NotNull
    private Long showId;
    @NotNull private Long hallId;
    @NotNull private LocalDateTime startTime;

}