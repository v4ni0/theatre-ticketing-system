package bg.uni.fmi.theatre.dto;

import bg.uni.fmi.theatre.domain.AgeRating;
import bg.uni.fmi.theatre.domain.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public AgeRating getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(AgeRating ageRating) {
        this.ageRating = ageRating;
    }
}
