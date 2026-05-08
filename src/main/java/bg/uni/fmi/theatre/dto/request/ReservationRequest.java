package bg.uni.fmi.theatre.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {

    @NotNull
    private Long performanceId;

    @NotBlank
    private String seatLabel;

    @NotBlank
    private String customerName;
}