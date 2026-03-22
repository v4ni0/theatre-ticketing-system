package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.domain.Genre;
import bg.uni.fmi.theatre.domain.Performance;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.repository.PerformanceRepository;
import bg.uni.fmi.theatre.repository.ShowRepository;
import bg.uni.fmi.theatre.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CatalogueService {
    private static final int DEFAULT_PAGE_SIZE = 1;
    private final PerformanceRepository performanceRepository;
    private final ShowRepository showRepository;
    private final int pageSize;

    @Autowired
    public CatalogueService(ShowRepository showRepository, PerformanceRepository performanceRepository) {
        this(showRepository, performanceRepository, DEFAULT_PAGE_SIZE);
    }

    public CatalogueService(ShowRepository showRepository, PerformanceRepository performanceRepository, int pageSize) {
        Validator.validateNotNull(performanceRepository, "performanceRepository must not be null");
        Validator.validateNotNull(showRepository, "showRepository must not be null");
        Validator.validatePositiveNumber(pageSize, "pageSize must be positive");
        this.performanceRepository = performanceRepository;
        this.showRepository = showRepository;
        this.pageSize = pageSize;
    }

    public Show addShow(Show show) {
        Validator.validateNotNull(show, "show must not be null");
        return showRepository.save(show);
    }

    public Optional<Show> findShowById(long id) {
        return showRepository.findById(id);
    }

    public List<Show> searchShows(String titleQuery, Genre genre, int page, int size) {
        Validator.validateNonNegativeNumber(page, "page must be non-negative");
        Validator.validatePositiveNumber(size, "size must be positive");
        return showRepository.findAll().stream()
            .filter(show -> titleQuery == null || show.title().toLowerCase().contains(titleQuery.toLowerCase()))
            .filter(show -> genre == null || show.genre() == genre)
            .sorted(Comparator.comparing(Show::title))
            .skip(page * size)
            .limit(size)
            .toList();
    }

    public Performance addPerformance(Performance performance) {
        Validator.validateNotNull(performance, "performance must not be null");
        if (!showRepository.existsById(performance.showId())) {
            throw new IllegalArgumentException("Show not found: " + performance.showId());
        }
        return performanceRepository.save(performance);
    }

    public List<Performance> findPerformancesByShow(long showId) {
        if (!showRepository.existsById(showId)) {
            throw new IllegalArgumentException("Show not found: " + showId);
        }
        return performanceRepository.findByShowId(showId);
    }
}
