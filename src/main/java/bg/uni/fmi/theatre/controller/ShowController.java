package bg.uni.fmi.theatre.controller;

import bg.uni.fmi.theatre.domain.AgeRating;
import bg.uni.fmi.theatre.domain.Genre;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.service.ShowService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowController {
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    public void displayAllShows() {
        showService.findAll().forEach(System.out::println);
    }

    public void updateShow(int id, String title, String description, Genre genre, int duration, AgeRating ageRating) {
        showService.updateShow(id, title, description, genre, duration, ageRating);
    }

    public List<Show> getShowsByGenre(Genre genre) {
        return showService.findByGenre(genre);
    }
}
