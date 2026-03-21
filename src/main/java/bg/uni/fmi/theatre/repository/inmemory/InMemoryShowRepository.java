package bg.uni.fmi.theatre.repository.inmemory;

import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.repository.ShowRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryShowRepository implements ShowRepository {
    private final Map<Long, Show> shows;
    private final AtomicLong isSequence = new AtomicLong(1);

    public InMemoryShowRepository() {
        this.shows = new HashMap<Long, Show>();
    }

    @Override
    public Show save(Show show) {
        shows.put(show.id(), show);
        return show;
    }

    @Override
    public Optional<Show> findById(long id) {
        return Optional.ofNullable(shows.get(id));
    }

    @Override
    public boolean existsById(long id) {
        return shows.containsKey(id);
    }

    @Override
    public void deleteById(long id) {
        shows.remove(id);
    }

    @Override
    public List<Show> findAll() {
        return shows.values().stream().toList();
    }

    public long nextId() {
        return isSequence.getAndIncrement();
    }
}
