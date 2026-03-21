package bg.uni.fmi.theatre.repository;

import bg.uni.fmi.theatre.domain.Show;

import java.util.List;
import java.util.Optional;

public interface ShowRepository {
    Show save(Show show);

    Optional<Show> findById(long id);

    boolean existsById(long id);

    void deleteById(long id);

    List<Show> findAll();
}
