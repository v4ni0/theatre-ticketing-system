package bg.uni.fmi.theatre.dto;

import bg.uni.fmi.theatre.domain.AgeRating;
import bg.uni.fmi.theatre.domain.Genre;
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
            show.id(),
            show.title(),
            show.description(),
            show.genre(),
            show.ageRating(),
            show.durationMinutes()
        );
    }
}
