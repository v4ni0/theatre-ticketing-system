package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.domain.Genre;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.dto.PageResponse;
import bg.uni.fmi.theatre.dto.ShowRequest;
import bg.uni.fmi.theatre.dto.ShowResponse;
import bg.uni.fmi.theatre.exception.NotFoundException;
import bg.uni.fmi.theatre.repository.ShowRepository;
import bg.uni.fmi.theatre.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {
    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public ShowResponse addShow(ShowRequest request) {
        Show show = new Show(showRepository.nextId(), request.getTitle(), request.getDescription(), request.getGenre(),
            request.getDurationMinutes(), request.getAgeRating());
        return ShowResponse.from(showRepository.save(show));
    }

    public List<ShowResponse> getAllShows() {
        return showRepository.findAll().stream().map(ShowResponse::from).toList();
    }

    public ShowResponse getShowById(Long id) {
        Show show = findById(id).orElseThrow(() -> new NotFoundException("Show not found: " + id));
        return ShowResponse.from(show);
    }

    public ShowResponse updateShow(Long id, ShowRequest request) {
        if (!showRepository.existsById(id)) {
            throw new NotFoundException("Show not found: " + id);
        }
        Show updated = new Show(
            id,
            request.getTitle(),
            request.getDescription(),
            request.getGenre(),
            request.getDurationMinutes(),
            request.getAgeRating()
        );
        showRepository.save(updated);
        return ShowResponse.from(updated);
    }

    public List<Show> findByGenre(Genre genre) {
        Validator.validateNotNull(genre, "genre must not be null");
        return showRepository.findAll().stream().filter(s -> s.genre() == genre).toList();
    }

    public Optional<Show> findById(long id) {
        return showRepository.findById(id);
    }

    public void deleteShow(Long id) {
        if (!showRepository.existsById(id)) {
            throw new NotFoundException("Show not found: " + id);
        }
        showRepository.deleteById(id);
    }

    public PageResponse<ShowResponse> searchShows(String titleQuery, Genre genre, Integer maxDurationMinutes, int page, int size) {
        Validator.validateNonNegativeNumber(page, "page must be non-negative");
        Validator.validatePositiveNumber(size, "size must be positive");
        List<ShowResponse> shows = showRepository.findAll().stream()
            .filter(show -> titleQuery == null || show.title().toLowerCase().contains(titleQuery.toLowerCase()))
            .filter(show -> genre == null || show.genre() == genre)
            .filter(show -> maxDurationMinutes == null || show.durationMinutes() <= maxDurationMinutes)
            .sorted(Comparator.comparing(Show::title))
            .skip(page * size)
            .limit(size)
            .map(ShowResponse::from)
            .toList();
        return new PageResponse<ShowResponse>(shows, page, size, shows.size());
    }

}
