package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.domain.AgeRating;
import bg.uni.fmi.theatre.domain.Genre;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.repository.ShowRepository;
import bg.uni.fmi.theatre.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowService {
    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public Show addShow(String title, String description, Genre genre, int durationMinutes, AgeRating ageRating) {
        Validator.validateString(title, "Show title is required");
        Validator.validateNotNull(genre, "Show genre is required");
        Validator.validateNotNull(ageRating, "Show ageRating is required");
        Validator.validatePositiveNumber(durationMinutes, "Show durationMinutes must be greater than 0");

        Show show = new Show(showRepository.nextId(), title, description, genre, durationMinutes, ageRating);
        return showRepository.save(show);
    }

    public List<Show> findAll() {
        return showRepository.findAll();
    }

    public Show updateShow(long id, String title, String description, Genre genre, int durationMinutes, AgeRating ageRating) {
        if (!showRepository.existsById(id)) {
            throw new IllegalArgumentException("Show not found: " + id);
        }
        Show updated = new Show(id, title, description, genre, durationMinutes, ageRating);
        return showRepository.save(updated);
    }

    public List<Show> findByGenre(Genre genre) {
        Validator.validateNotNull(genre, "genre must not be null");
        return showRepository.findAll().stream().filter(s -> s.genre() == genre).toList();
    }

    public Optional<Show> findById(long id) {
        return showRepository.findById(id);
    }
}
