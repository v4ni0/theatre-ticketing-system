package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.domain.AgeRating;
import bg.uni.fmi.theatre.domain.Genre;
import bg.uni.fmi.theatre.repository.ShowRepository;
import bg.uni.fmi.theatre.repository.inmemory.InMemoryShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShowServiceTest {
    private ShowService showService;
    private ShowRepository inMemoryShowRepository;

    @BeforeEach
    public void setUp() {
        inMemoryShowRepository = new InMemoryShowRepository();
        showService = new ShowService(inMemoryShowRepository);
    }

    @Test
    void testUpdateShowExistingShowIsUpdated() {
        showService.addShow("title", "description", Genre.COMEDY, 90, AgeRating.ALL);
        showService.updateShow(1, "Updated Title", "Updated Description", Genre.DRAMA, 120, AgeRating.PG_16);
        var updatedShow = showService.findById(1).orElseThrow();
        assertEquals("Updated Title", updatedShow.title());
        assertEquals("Updated Description", updatedShow.description());
        assertEquals(Genre.DRAMA, updatedShow.genre());
        assertEquals(120, updatedShow.durationMinutes());
        assertEquals(AgeRating.PG_16, updatedShow.ageRating());
    }

}