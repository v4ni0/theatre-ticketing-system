package bg.uni.fmi.theatre.repository.inmemory;

import bg.uni.fmi.theatre.domain.Show;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryShowRepository {
    private final Map<Long, Show> shows;
    private final AtomicLong idSequence = new AtomicLong(1);

    public InMemoryShowRepository() {
        this.shows = new HashMap<>();
    }

    public Show save(Show show) {
        shows.put(show.getId(), show);
        return show;
    }

    public Optional<Show> findById(Long id) {
        return Optional.ofNullable(shows.get(id));
    }

    public boolean existsById(Long id) {
        return shows.containsKey(id);
    }

    public void deleteById(Long id) {
        shows.remove(id);
    }

    public List<Show> findAll() {
        return shows.values().stream().toList();
    }

    public long nextId() {
        return idSequence.getAndIncrement();
    }
}
