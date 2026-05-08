package bg.uni.fmi.theatre.domain;

import bg.uni.fmi.theatre.validation.Validator;
import bg.uni.fmi.theatre.vo.AgeRating;
import bg.uni.fmi.theatre.vo.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "show")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_rating")
    private AgeRating ageRating;

    public Show(String title, String description, Genre genre, int durationMinutes, AgeRating ageRating) {
        Validator.validateString(title, "Show title cannot be null or blank");
        Validator.validatePositiveNumber(durationMinutes, "Show duration must be positive");
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.ageRating = ageRating;
    }

}