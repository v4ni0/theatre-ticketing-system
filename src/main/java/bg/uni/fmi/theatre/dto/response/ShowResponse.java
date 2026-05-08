package bg.uni.fmi.theatre.dto.response;

import bg.uni.fmi.theatre.vo.AgeRating;
import bg.uni.fmi.theatre.vo.Genre;
import bg.uni.fmi.theatre.domain.Show;

public record ShowResponse(
    Long id,
    String title,
    String description,
    Genre genre,
    AgeRating ageRating,
    Integer durationMinutes
) {
    public static ShowResponse from(Show show) {
        return new ShowResponse(
            show.getId(),
            show.getTitle(),
            show.getDescription(),
            show.getGenre(),
            show.getAgeRating(),
            show.getDurationMinutes()
        );
    }
}
