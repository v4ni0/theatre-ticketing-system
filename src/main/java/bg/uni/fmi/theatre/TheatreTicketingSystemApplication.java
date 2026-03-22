package bg.uni.fmi.theatre;

import bg.uni.fmi.theatre.controller.ShowController;
import bg.uni.fmi.theatre.domain.AgeRating;
import bg.uni.fmi.theatre.domain.Genre;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TheatreTicketingSystemApplication implements CommandLineRunner {
    @Autowired
    private ShowController showController;

    @Autowired
    private ShowService showService;

    public static void main(String[] args) {
        SpringApplication.run(TheatreTicketingSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Application started successfully!");
        showService.addShow("Hamlet", "Drama", Genre.DRAMA, 180, AgeRating.PG_16);
        showService.addShow("The Nutcracker", "Ballet", Genre.BALLET, 150, AgeRating.ALL);
        try {
            showService.addShow("", "Comedy", Genre.COMEDY, 120, AgeRating.ALL);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
        }
        System.out.println("---------------------------------------");
        System.out.println("✅ Shows added successfully!");
        System.out.println("---------------------------------------");

        System.out.println("📌 Displaying all shows:");
        showController.displayAllShows();
        System.out.println("---------------------------------------");

        System.out.println("🔄 Updating 'Hamlet' duration to 200 minutes...");
        showController.updateShow(1, "Hamlet", "Drama", Genre.DRAMA, 200, AgeRating.PG_16);

        System.out.println("---------------------------------------");
        System.out.println("📌 Displaying updated shows:");
        showController.displayAllShows();

        System.out.println("---------------------------------------");
        System.out.println("📌 Displaying all shows by genre 'Drama':");
        List<Show> dramaShows = showController.getShowsByGenre(Genre.DRAMA);
        dramaShows.stream().forEach(System.out::println);
        System.out.println("---------------------------------------");
    }

}
