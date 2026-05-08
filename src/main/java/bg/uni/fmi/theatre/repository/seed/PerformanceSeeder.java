package bg.uni.fmi.theatre.repository.seed;

import bg.uni.fmi.theatre.domain.Hall;
import bg.uni.fmi.theatre.domain.Performance;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.repository.HallRepository;
import bg.uni.fmi.theatre.repository.PerformanceRepository;
import bg.uni.fmi.theatre.repository.ShowRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(2)
public class PerformanceSeeder implements CommandLineRunner {

    private final PerformanceRepository performances;
    private final ShowRepository shows;
    private final HallRepository halls;

    public PerformanceSeeder(PerformanceRepository performances, ShowRepository shows, HallRepository halls) {
        this.performances = performances;
        this.shows = shows;
        this.halls = halls;
    }

    @Override
    public void run(String... args) {
        if (performances.count() > 0) {
            return;
        }

        Show hamlet = shows.findAll().stream()
                .filter(show -> show.getTitle().equals("Hamlet"))
                .findFirst().orElseThrow();
        Hall mainStage = halls.findAll().stream()
                .filter(h -> h.getName().equals("Main Stage"))
                .findFirst().orElseThrow();

        performances.save(
                new Performance(
                        hamlet,
                        mainStage,
                        LocalDateTime.of(2026, 6, 15, 19, 0)));
        performances.save(
                new Performance(
                        hamlet,
                        mainStage,
                        LocalDateTime.of(2026, 6, 22, 19, 0)));
    }
}