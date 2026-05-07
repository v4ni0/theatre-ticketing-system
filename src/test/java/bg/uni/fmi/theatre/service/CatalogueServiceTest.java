package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.domain.*;
import bg.uni.fmi.theatre.exception.ValidationException;
import bg.uni.fmi.theatre.repository.ShowRepository;
import bg.uni.fmi.theatre.repository.inmemory.InMemoryPerformanceRepository;
import bg.uni.fmi.theatre.vo.AgeRating;
import bg.uni.fmi.theatre.vo.Genre;
import bg.uni.fmi.theatre.vo.PerformanceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogueServiceTest {
    @Mock
    private ShowRepository showRepo;
    private InMemoryPerformanceRepository perfRepo;
    private CatalogueService service;

    @BeforeEach
    void setUp() {
        perfRepo = new InMemoryPerformanceRepository();
        service = new CatalogueService(showRepo, perfRepo, 5);
    }

    @Test
    void testAddShowValidShowIsSaved() {
        Show show = new Show(1L, "Hamlet", "Classic drama", Genre.DRAMA, 120, AgeRating.PG_16);
        when(showRepo.save(show)).thenReturn(show);
        when(showRepo.findById(1L)).thenReturn(Optional.of(show));
        service.addShow(show);
        Optional<Show> found = service.findShowById(show.getId());
        assertTrue(found.isPresent());
        assertEquals("Hamlet", found.get().getTitle());
    }

    @Test
    void testAddShowNullShowThrows() {
        assertThrows(ValidationException.class, () -> service.addShow(null));
    }

    @Test
    void testSearchShowsByTitleReturnsMatchingShows() {
        when(showRepo.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Othello", "Drama", Genre.DRAMA, 110, AgeRating.PG_16),
            new Show(3L, "A Midsummer Night's Dream", "Comedy", Genre.COMEDY, 90, AgeRating.ALL)
        ));
        List<Show> results = service.searchShows("ham", null, 0, 10);
        assertEquals(1, results.size());
        assertEquals("Hamlet", results.get(0).getTitle());
    }

    @Test
    void testSearchShowsByTitleReturnsMatchingShowsSecondPage() {
        when(showRepo.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Hamlet2", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(3L, "Othello", "Drama", Genre.DRAMA, 110, AgeRating.PG_16),
            new Show(4L, "A Midsummer Night's Dream", "Comedy", Genre.COMEDY, 90, AgeRating.ALL)
        ));
        List<Show> results = service.searchShows("ham", null, 1, 1);
        assertEquals(1, results.size());
        assertEquals("Hamlet2", results.get(0).getTitle());
    }

    @Test
    void testSearchShowsByGenreReturnsMatchingShows() {
        when(showRepo.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Chicago", "Musical", Genre.MUSICAL, 130, AgeRating.PG_12)
        ));
        List<Show> results = service.searchShows(null, Genre.MUSICAL, 0, 10);
        assertEquals(1, results.size());
        assertEquals("Chicago", results.get(0).getTitle());
    }

    @Test
    void testSearchShowsCaseInsensitiveReturnsResults() {
        when(showRepo.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16)
        ));
        List<Show> results = service.searchShows("HAMLET", null, 0, 10);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchShowsEmptyQueryReturnsAllShows() {
        when(showRepo.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Chicago", "Musical", Genre.MUSICAL, 130, AgeRating.PG_12)
        ));
        List<Show> results = service.searchShows("", null, 0, 10);
        assertEquals(2, results.size());
    }

    @Test
    void testSearchShowsPageOutOfBoundsReturnsEmptyList() {
        when(showRepo.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16)
        ));
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
        List<Show> allShows = List.of(
            new Show(1L, "Show 1", "Desc", Genre.DRAMA, 90, AgeRating.ALL),
            new Show(2L, "Show 2", "Desc", Genre.DRAMA, 90, AgeRating.ALL),
            new Show(3L, "Show 3", "Desc", Genre.DRAMA, 90, AgeRating.ALL),
            new Show(4L, "Show 4", "Desc", Genre.DRAMA, 90, AgeRating.ALL),
            new Show(5L, "Show 5", "Desc", Genre.DRAMA, 90, AgeRating.ALL),
            new Show(6L, "Show 6", "Desc", Genre.DRAMA, 90, AgeRating.ALL),
            new Show(7L, "Show 7", "Desc", Genre.DRAMA, 90, AgeRating.ALL),
            new Show(8L, "Show 8", "Desc", Genre.DRAMA, 90, AgeRating.ALL)
        );
        when(showRepo.findAll()).thenReturn(allShows);
        List<Show> page0 = service.searchShows(null, null, 0, 3);
        List<Show> page1 = service.searchShows(null, null, 1, 3);
        List<Show> page2 = service.searchShows(null, null, 2, 3);
        assertEquals(3, page0.size());
        assertEquals(3, page1.size());
        assertEquals(2, page2.size());
    }

    @Test
    void testAddPerformanceUnknownShow() {
        when(showRepo.existsById(999L)).thenReturn(false);
        Performance p = new Performance(1L, 999L, 1L, LocalDateTime.now().plusDays(1), PerformanceStatus.SCHEDULED);
        assertThrows(IllegalArgumentException.class, () -> service.addPerformance(p));
    }

    @Test
    void testFindPerformancesByShowValidShowReturnsPerformances() {
        Show show = new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16);
        when(showRepo.existsById(1L)).thenReturn(true);
        service.addPerformance(new Performance(perfRepo.nextId(), show.getId(), 1L, LocalDateTime.now().plusDays(1), PerformanceStatus.SCHEDULED));
        service.addPerformance(new Performance(perfRepo.nextId(), show.getId(), 1L, LocalDateTime.now().plusDays(2), PerformanceStatus.SCHEDULED));
        List<Performance> performances = service.findPerformancesByShow(show.getId());
        assertEquals(2, performances.size());
    }
}
