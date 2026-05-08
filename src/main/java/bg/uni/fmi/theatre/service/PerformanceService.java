package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.domain.Hall;
import bg.uni.fmi.theatre.dto.request.PerformanceRequest;
import bg.uni.fmi.theatre.dto.response.PerformanceResponse;
import bg.uni.fmi.theatre.domain.Performance;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.exception.NotFoundException;
import bg.uni.fmi.theatre.repository.HallRepository;
import bg.uni.fmi.theatre.repository.PerformanceRepository;
import bg.uni.fmi.theatre.repository.ShowRepository;
import bg.uni.fmi.theatre.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceService {
    private static final int DEFAULT_PAGE_SIZE = 1;
    private final PerformanceRepository performanceRepository;
    private final ShowRepository showRepository;
    private final HallRepository hallRepository;
    private final int pageSize;

    @Autowired
    public PerformanceService(ShowRepository showRepository, PerformanceRepository performanceRepository, HallRepository hallRepository) {
        this(showRepository, performanceRepository, hallRepository, DEFAULT_PAGE_SIZE);
    }

    public PerformanceService(ShowRepository showRepository, PerformanceRepository performanceRepository, HallRepository hallRepository, Integer pageSize) {
        Validator.validateNotNull(performanceRepository, "performanceRepository must not be null");
        Validator.validateNotNull(showRepository, "showRepository must not be null");
        Validator.validatePositiveNumber(pageSize, "pageSize must be positive");
        this.performanceRepository = performanceRepository;
        this.showRepository = showRepository;
        this.hallRepository = hallRepository;
        this.pageSize = pageSize;
    }

    public PerformanceResponse addPerformance(PerformanceRequest request) {
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new NotFoundException("Show not found" + request.getShowId()));
        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new NotFoundException("Hall not found" + request.getHallId()));

        Performance performance = new Performance(show, hall, request.getStartTime());
        Performance saved = performanceRepository.save(performance);
        return PerformanceResponse.from(saved);
    }

    public List<Performance> findPerformancesByShow(long showId) {
        if (!showRepository.existsById(showId)) {
            throw new IllegalArgumentException("Show not found: " + showId);
        }
        return performanceRepository.findByShowId(showId);
    }
}
