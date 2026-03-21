package bg.uni.fmi.theatre.repository.inmemory;

import bg.uni.fmi.theatre.domain.Performance;
import bg.uni.fmi.theatre.repository.PerformanceRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPerformanceRepository implements PerformanceRepository {
    private final Map<Long, Performance> performances;
    private final AtomicLong isSequence;

    public InMemoryPerformanceRepository() {
        this.performances = new HashMap<>();
        this.isSequence = new AtomicLong(1);
    }

    @Override
    public Performance save(Performance performance) {
        performances.put(performance.id(), performance);
        return performance;
    }

    @Override
    public Optional<Performance> findById(Long id) {
        return Optional.ofNullable(performances.get(id));
    }

    @Override
    public List<Performance> findAll() {
        return performances.values().stream().toList();
    }

    @Override
    public List<Performance> findByShowId(Long showId) {
        return performances.values()
            .stream()
            .filter(performance -> performance.showId().equals(showId))
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        performances.remove(id);
    }

    public long nextId() {
        return isSequence.getAndIncrement();
    }
}
