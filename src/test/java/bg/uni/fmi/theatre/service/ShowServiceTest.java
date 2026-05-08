package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.vo.AgeRating;
import bg.uni.fmi.theatre.vo.Genre;
import bg.uni.fmi.theatre.domain.Show;
import bg.uni.fmi.theatre.dto.response.PageResponse;
import bg.uni.fmi.theatre.dto.request.ShowRequest;
import bg.uni.fmi.theatre.dto.response.ShowResponse;
import bg.uni.fmi.theatre.exception.NotFoundException;
import bg.uni.fmi.theatre.exception.ValidationException;
import bg.uni.fmi.theatre.repository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {
    @Mock
    private ShowRepository showRepository;
    private ShowService showService;

    @BeforeEach
    void setUp() {
        showService = new ShowService(showRepository);
    }

    // --- addShow ---

    @Test
    void addShowSavesAndReturnsResponse() {
        ShowRequest req = request("Hamlet", "A tragedy", Genre.DRAMA, 180, AgeRating.PG_16);
        Show saved = new Show(1L, "Hamlet", "A tragedy", Genre.DRAMA, 180, AgeRating.PG_16);
        when(showRepository.save(any(Show.class))).thenReturn(saved);

        ShowResponse response = showService.addShow(req);

        assertEquals(1L, response.id());
        assertEquals("Hamlet", response.title());
        assertEquals(Genre.DRAMA, response.genre());
        verify(showRepository).save(any(Show.class));
    }

    @Test
    void addShowPassesNullIdToRepository() {
        ShowRequest req = request("Hamlet", null, Genre.DRAMA, 120, AgeRating.ALL);
        Show saved = new Show(1L, "Hamlet", null, Genre.DRAMA, 120, AgeRating.ALL);
        when(showRepository.save(any(Show.class))).thenReturn(saved);

        showService.addShow(req);

        verify(showRepository).save(argThat(show -> show.getId() == null));
    }

    // --- getAllShows ---

    @Test
    void getAllShowsReturnsAllMappedResponses() {
        when(showRepository.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Chicago", "Musical", Genre.MUSICAL, 130, AgeRating.PG_12)
        ));

        List<ShowResponse> result = showService.getAllShows();

        assertEquals(2, result.size());
        assertEquals("Hamlet", result.get(0).title());
        assertEquals("Chicago", result.get(1).title());
    }

    @Test
    void getAllShowsReturnsEmptyListWhenNoShows() {
        when(showRepository.findAll()).thenReturn(List.of());

        assertTrue(showService.getAllShows().isEmpty());
    }

    // --- getShowById ---

    @Test
    void getShowByIdReturnsResponseWhenFound() {
        Show show = new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16);
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));

        ShowResponse response = showService.getShowById(1L);

        assertEquals(1L, response.id());
        assertEquals("Hamlet", response.title());
    }

    @Test
    void getShowByIdThrowsNotFoundWhenMissing() {
        when(showRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> showService.getShowById(99L));
    }

    // --- updateShow ---

    @Test
    void updateShowSavesAndReturnsUpdatedResponse() {
        ShowRequest req = request("Hamlet Updated", "New desc", Genre.DRAMA, 150, AgeRating.PG_16);
        Show updated = new Show(1L, "Hamlet Updated", "New desc", Genre.DRAMA, 150, AgeRating.PG_16);
        when(showRepository.existsById(1L)).thenReturn(true);
        when(showRepository.save(any(Show.class))).thenReturn(updated);

        ShowResponse response = showService.updateShow(1L, req);

        assertEquals("Hamlet Updated", response.title());
        assertEquals(150, response.durationMinutes());
    }

    @Test
    void updateShowThrowsNotFoundWhenMissing() {
        when(showRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class,
            () -> showService.updateShow(99L, request("X", null, Genre.DRAMA, 60, AgeRating.ALL)));
    }

    // --- deleteShow ---

    @Test
    void deleteShowRemovesExistingShow() {
        when(showRepository.existsById(1L)).thenReturn(true);

        showService.deleteShow(1L);

        verify(showRepository).deleteById(1L);
    }

    @Test
    void deleteShowThrowsNotFoundWhenMissing() {
        when(showRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> showService.deleteShow(99L));
        verify(showRepository, never()).deleteById(any());
    }

    // --- findByGenre ---

    @Test
    void findByGenreReturnsMatchingShows() {
        when(showRepository.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Chicago", "Musical", Genre.MUSICAL, 130, AgeRating.PG_12)
        ));

        List<Show> result = showService.findByGenre(Genre.DRAMA);

        assertEquals(1, result.size());
        assertEquals("Hamlet", result.get(0).getTitle());
    }

    @Test
    void findByGenreNullThrowsValidationException() {
        assertThrows(ValidationException.class, () -> showService.findByGenre(null));
    }

    // --- searchShows (ported from CatalogueServiceTest) ---

    @Test
    void searchShowsByTitleReturnsMatchingShows() {
        when(showRepository.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Othello", "Drama", Genre.DRAMA, 110, AgeRating.PG_16),
            new Show(3L, "A Midsummer Night's Dream", "Comedy", Genre.COMEDY, 90, AgeRating.ALL)
        ));

        PageResponse<ShowResponse> result = showService.searchShows("ham", null, null, 0, 10);

        assertEquals(1, result.content().size());
        assertEquals("Hamlet", result.content().get(0).title());
    }

    @Test
    void searchShowsByTitleReturnsMatchingShowsSecondPage() {
        when(showRepository.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Hamlet2", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(3L, "Othello", "Drama", Genre.DRAMA, 110, AgeRating.PG_16)
        ));

        PageResponse<ShowResponse> result = showService.searchShows("ham", null, null, 1, 1);

        assertEquals(1, result.content().size());
        assertEquals("Hamlet2", result.content().get(0).title());
    }

    @Test
    void searchShowsByGenreReturnsMatchingShows() {
        when(showRepository.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Chicago", "Musical", Genre.MUSICAL, 130, AgeRating.PG_12)
        ));

        PageResponse<ShowResponse> result = showService.searchShows(null, Genre.MUSICAL, null, 0, 10);

        assertEquals(1, result.content().size());
        assertEquals("Chicago", result.content().get(0).title());
    }

    @Test
    void searchShowsCaseInsensitiveReturnsResults() {
        when(showRepository.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16)
        ));

        PageResponse<ShowResponse> result = showService.searchShows("HAMLET", null, null, 0, 10);

        assertEquals(1, result.content().size());
    }

    @Test
    void searchShowsEmptyQueryReturnsAllShows() {
        when(showRepository.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16),
            new Show(2L, "Chicago", "Musical", Genre.MUSICAL, 130, AgeRating.PG_12)
        ));

        PageResponse<ShowResponse> result = showService.searchShows("", null, null, 0, 10);

        assertEquals(2, result.content().size());
    }

    @Test
    void searchShowsPageOutOfBoundsReturnsEmptyList() {
        when(showRepository.findAll()).thenReturn(List.of(
            new Show(1L, "Hamlet", "Drama", Genre.DRAMA, 120, AgeRating.PG_16)
        ));

        PageResponse<ShowResponse> result = showService.searchShows(null, null, null, 5, 10);

        assertTrue(result.content().isEmpty());
    }

    @Test
    void searchShowsNegativePageThrowsValidationException() {
        assertThrows(ValidationException.class,
            () -> showService.searchShows(null, null, null, -1, 10));
    }

    @Test
    void searchShowsZeroSizeThrowsValidationException() {
        assertThrows(ValidationException.class,
            () -> showService.searchShows(null, null, null, 0, 0));
    }

    @Test
    void searchShowsPaginationReturnsCorrectPages() {
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
        when(showRepository.findAll()).thenReturn(allShows);

        assertEquals(3, showService.searchShows(null, null, null, 0, 3).content().size());
        assertEquals(3, showService.searchShows(null, null, null, 1, 3).content().size());
        assertEquals(2, showService.searchShows(null, null, null, 2, 3).content().size());
    }

    @Test
    void searchShowsByMaxDurationFiltersCorrectly() {
        when(showRepository.findAll()).thenReturn(List.of(
            new Show(1L, "Short Play", "Desc", Genre.COMEDY, 60, AgeRating.ALL),
            new Show(2L, "Long Play", "Desc", Genre.DRAMA, 180, AgeRating.PG_16)
        ));

        PageResponse<ShowResponse> result = showService.searchShows(null, null, 90, 0, 10);

        assertEquals(1, result.content().size());
        assertEquals("Short Play", result.content().get(0).title());
    }

    // --- helper ---

    private ShowRequest request(String title, String description, Genre genre, int duration, AgeRating ageRating) {
        ShowRequest req = new ShowRequest();
        req.setTitle(title);
        req.setDescription(description);
        req.setGenre(genre);
        req.setDurationMinutes(duration);
        req.setAgeRating(ageRating);
        return req;
    }
}
