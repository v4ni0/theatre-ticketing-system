package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.domain.*;
import bg.uni.fmi.theatre.exception.ValidationException;
import bg.uni.fmi.theatre.repository.inmemory.InMemoryPerformanceRepository;
import bg.uni.fmi.theatre.repository.inmemory.InMemoryShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CatalogueServiceTest {
    private InMemoryShowRepository showRepo;
    private InMemoryPerformanceRepository perfRepo;
    private CatalogueService service;

    @BeforeEach
    void setUp() {
        showRepo = new InMemoryShowRepository();
        perfRepo = new InMemoryPerformanceRepository();
        service = new CatalogueService(showRepo, perfRepo, 5);
    }

    @Test
    void testAddShowValidShowIsSaved() {
        Show show = new Show(showRepo.nextId(), "Hamlet", "Classic drama", Genre.DRAMA, 120, AgeRating.PG_16);
        service.addShow(show);
        Optional<Show> found = service.findShowById(show.id());
        assertTrue(found.isPresent());
        assertEquals("Hamlet", found.get().title());
    }

    @Test
    void testAddShowNullShowThrows() {
        assertThrows(ValidationException.class, () -> service.addShow(null));
    }

    @Test
    void testSearchShowsByTitleReturnsMatchingShows() {
        service.addShow(new Show(showRepo.nextId(), "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16));
        service.addShow(new Show(showRepo.nextId(), "Othello", "Drama", Genre.DRAMA, 110, AgeRating.PG_16));
        service.addShow(new Show(showRepo.nextId(), "A Midsummer Night's Dream", "Comedy", Genre.COMEDY, 90, AgeRating.ALL));
        List<Show> results = service.searchShows("ham", null, 0, 10);
        assertEquals(1, results.size());
        assertEquals("Hamlet", results.get(0).title());
    }
    @Test
    void testSearchShowsByTitleReturnsMatchingShowsSecondPage() {
        service.addShow(new Show(showRepo.nextId(), "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16));
        service.addShow(new Show(showRepo.nextId(), "Hamlet2", "Drama", Genre.DRAMA, 120, AgeRating.PG_16));
        service.addShow(new Show(showRepo.nextId(), "Othello", "Drama", Genre.DRAMA, 110, AgeRating.PG_16));
        service.addShow(new Show(showRepo.nextId(), "A Midsummer Night's Dream", "Comedy", Genre.COMEDY, 90, AgeRating.ALL));
        List<Show> results = service.searchShows("ham", null, 1 , 1);
        System.out.println(results);
        assertEquals(1, results.size());
        assertEquals("Hamlet2", results.get(0).title());
    }

    @Test
    void testSearchShowsByGenreReturnsMatchingShows() {
        service.addShow(new Show(showRepo.nextId(), "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16));
        service.addShow(new Show(showRepo.nextId(), "Chicago", "Musical", Genre.MUSICAL, 130, AgeRating.PG_12));
        List<Show> results = service.searchShows(null, Genre.MUSICAL, 0, 10);
        assertEquals(1, results.size());
        assertEquals("Chicago", results.get(0).title());
    }

    @Test
    void testSearchShowsCaseInsensitiveReturnsResults() {
        service.addShow(new Show(showRepo.nextId(), "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16));
        List<Show> results = service.searchShows("HAMLET", null, 0, 10);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchShowsEmptyQueryReturnsAllShows() {
        service.addShow(new Show(showRepo.nextId(), "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16));
        service.addShow(new Show(showRepo.nextId(), "Chicago", "Musical", Genre.MUSICAL, 130, AgeRating.PG_12));
        List<Show> results = service.searchShows("", null, 0, 10);
        assertEquals(2, results.size());
    }

    @Test
    void testSearchShowsPageOutOfBoundsReturnsEmptyList() {
        service.addShow(new Show(showRepo.nextId(), "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16));
        List<Show> results = service.searchShows(null, null, 5, 10);
        assertTrue(results.isEmpty());
    }

    @Test
    void testSearchShowsNegativePageThrows() {
        assertThrows(ValidationException.class, () -> service.searchShows(null, null, -1, 10));
    }

    @Test
    void testSearchShowsZeroSizeThrows() {
        assertThrows(ValidationException.class, () -> service.searchShows(null, null, 0, 0));
    }

    @Test
    void testSearchShowsPaginationReturnsCorrectPage() {
        for (int i = 1; i <= 8; i++) {
            service.addShow(new Show((long) i, "Show " + i, "Desc", Genre.DRAMA, 90, AgeRating.ALL));
        }
        List<Show> page0 = service.searchShows(null, null, 0, 3);
        List<Show> page1 = service.searchShows(null, null, 1, 3);
        List<Show> page2 = service.searchShows(null, null, 2, 3);
        assertEquals(3, page0.size());
        assertEquals(3, page1.size());
        assertEquals(2, page2.size());
    }

    @Test
    void testAddPerformanceUnknownShow() {
        Performance p = new Performance(1L, 999L, 1L, LocalDateTime.now().plusDays(1), PerformanceStatus.SCHEDULED);
        assertThrows(IllegalArgumentException.class, () -> service.addPerformance(p));
    }

    @Test
    void testFindPerformancesByShowValidShowReturnsPerformances() {
        Show show = new Show(showRepo.nextId(), "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16);
        service.addShow(show);
        service.addPerformance(new Performance(perfRepo.nextId(), show.id(), 1L, LocalDateTime.now().plusDays(1), PerformanceStatus.SCHEDULED));
        service.addPerformance(new Performance(perfRepo.nextId(), show.id(), 1L, LocalDateTime.now().plusDays(2), PerformanceStatus.SCHEDULED));
        List<Performance> performances = service.findPerformancesByShow(show.id());
        assertEquals(2, performances.size());
    }
}