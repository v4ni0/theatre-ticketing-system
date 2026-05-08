package bg.uni.fmi.theatre.dto.request;

import bg.uni.fmi.theatre.vo.AgeRating;
import bg.uni.fmi.theatre.vo.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowRequest {
    private static final int MAX_TITLE_SIZE = 100;
    @NotBlank(message = "title is required")
    @Size(max = MAX_TITLE_SIZE, message = "title must be at most 100 characters")
    private String title;
    private String description;
    private Genre genre;
    @Positive(message = "durationMinutes must be positive")
    private int durationMinutes;
    private AgeRating ageRating;
}
