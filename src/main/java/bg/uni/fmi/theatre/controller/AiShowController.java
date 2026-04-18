package bg.uni.fmi.theatre.controller;

import bg.uni.fmi.theatre.domain.Genre;
import bg.uni.fmi.theatre.dto.PageResponse;
import bg.uni.fmi.theatre.dto.SearchFiltersResponse;
import bg.uni.fmi.theatre.dto.ShowResponse;
import bg.uni.fmi.theatre.dto.ShowComparisonResponse;
import bg.uni.fmi.theatre.dto.ShowSummaryResponse;
import bg.uni.fmi.theatre.service.AiShowService;
import bg.uni.fmi.theatre.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/shows")
public class AiShowController {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    private final AiShowService aiShowService;
    private final ShowService showService;

    public AiShowController(AiShowService aiShowService, ShowService showService) {
        this.aiShowService = aiShowService;
        this.showService = showService;
    }

    @GetMapping("/{id}/summary")
    @Operation(summary = "Ai generated show summary")
    public ShowSummaryResponse showSummary(@PathVariable Long id) {
        return aiShowService.generateSummary(id);
    }

    @GetMapping("/search")
    public PageResponse<ShowResponse> naturalLanguageSearch(@RequestParam String query) {
        SearchFiltersResponse filters = aiShowService.parseSearchQuery(query);
        Genre genre = null;
        if (filters.genre() != null) {
            try {
                genre = Genre.valueOf(filters.genre().strip().toUpperCase());
            }
            catch (IllegalArgumentException ignored) {}
        }
        return showService.searchShows(filters.titleKeyword(), genre, filters.maxDurationMinutes(), DEFAULT_PAGE, DEFAULT_SIZE);
    }

    @GetMapping("/compare")
    @Operation(summary = "Compare two shows for an occasion")
    public ShowComparisonResponse compareShows(
            @RequestParam Long showId1,
            @RequestParam Long showId2,
            @RequestParam String occasion) {
        return aiShowService.compareShows(showId1, showId2, occasion);
    }
}
