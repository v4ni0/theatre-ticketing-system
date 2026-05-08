package bg.uni.fmi.theatre.repository;

import bg.uni.fmi.theatre.domain.Hall;
import bg.uni.fmi.theatre.domain.Performance;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.vo.AgeRating;
import bg.uni.fmi.theatre.vo.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PerformanceRepositoryTest {

    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private HallRepository hallRepository;

    @Test
    void findByShowId_returnsOnlyMatchingPerformances() {
        Show hamlet = showRepository.save(
                new Show("Hamlet", "tragedy", Genre.DRAMA, 180, AgeRating.PG_16));
        Show chicago = showRepository.save(
                new Show("Chicago", "musical", Genre.MUSICAL, 135, AgeRating.PG_12));
        Hall hall = hallRepository.save(new Hall("Main Stage", 500));

        performanceRepository.save(new Performance(hamlet, hall,
                LocalDateTime.of(2026, 7, 1, 19, 0)));
        performanceRepository.save(new Performance(hamlet, hall,
                LocalDateTime.of(2026, 7, 8, 19, 0)));
        performanceRepository.save(new Performance(chicago, hall,
                LocalDateTime.of(2026, 7, 2, 20, 0)));

        List<Performance> hamletPerformances =
                performanceRepository.findByShowId(hamlet.getId());

        assertEquals(2, hamletPerformances.size(), "Size should be 2");
        assertTrue(hamletPerformances.stream()
                .allMatch(p -> p.getShow().getId().equals(hamlet.getId())));
    }

    @Test
    void save_setsIdAndPreservesRelationships() {
        Show show = showRepository.save(
                new Show("Swan Lake", "ballet", Genre.BALLET, 140, AgeRating.ALL));
        Hall hall = hallRepository.save(new Hall("Studio", 120));

        Performance saved = performanceRepository.save(
                new Performance(show, hall, LocalDateTime.of(2026, 8, 1, 18, 0)));

        assertNotNull(saved.getId());
        assertEquals("Swan Lake", saved.getShow().getTitle());
        assertEquals("Studio", saved.getHall().getName());
    }
}