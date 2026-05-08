package bg.uni.fmi.theatre.repository.seed;

import bg.uni.fmi.theatre.repository.ShowRepository;
import bg.uni.fmi.theatre.vo.AgeRating;
import bg.uni.fmi.theatre.vo.Genre;
import bg.uni.fmi.theatre.domain.Show;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ShowSeeder implements CommandLineRunner {

    private final ShowRepository shows;

    public ShowSeeder(ShowRepository shows) {
        this.shows = shows;
    }

    @Override
    public void run(String... args) {
        if (shows.count() > 0) {
            return;
        }

        shows.save(new Show("Hamlet",
                "William Shakespeare's timeless tragedy about a Danish prince seeking revenge.",
                Genre.DRAMA,
                180,
                AgeRating.PG_16));

        shows.save(new Show(
                "Chicago",
                "Set in the jazz age of the 1920s, a tale of fame, murder and corruption.",
                Genre.MUSICAL,
                135,
                AgeRating.PG_12));

        shows.save(new Show("Othello",
                "Shakespeare's powerful tragedy of jealousy and betrayal.",
                Genre.DRAMA,
                150,
                AgeRating.PG_16));

        shows.save(new Show("A Midsummer Night's Dream",
                "A magical comedy of love and mischief in an enchanted forest.",
                Genre.COMEDY,
                110,
                AgeRating.ALL));

        shows.save(new Show("The Phantom of the Opera",
                "A mysterious masked figure haunts the Paris Opera House.",
                Genre.MUSICAL,
                150,
                AgeRating.PG_12));
    }
}
