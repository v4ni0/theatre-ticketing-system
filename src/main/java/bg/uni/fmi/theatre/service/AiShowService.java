package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.dto.SearchFiltersResponse;
import bg.uni.fmi.theatre.dto.ShowComparisonResponse;
import bg.uni.fmi.theatre.dto.ShowResponse;
import bg.uni.fmi.theatre.dto.ShowSummaryResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiShowService {
    private final ShowService showService;
    private final ChatClient chatClient;

    public AiShowService(ShowService showService, ChatClient.Builder builder) {
        this.chatClient = builder
            .defaultSystem("You are a helpful theatre assistant. "
                + "You recommend shows based on user preferences. "
            )
            .build();
        this.showService = showService;
    }

    public ShowSummaryResponse generateSummary(Long id) {
        ShowResponse show = showService.getShowById(id);
        return chatClient.prompt()
            .user("Generate a summary for this theatre event:\n"
            + "Title: " + show.title() + "\n"
            + "Genre: " + show.genre() + "\n"
            + "Duration: " + show.durationMinutes() + " minutes\n"
            + "Age rating: " + show.ageRating() + "\n\n"
            + "Write a 2-3 sentence summary, identify the target audience, "
            + "and list 2-4 highlights.")
            .call()
            .entity(ShowSummaryResponse.class);
    }
    public SearchFiltersResponse parseSearchQuery(String naturalLanguageQuery) {
        String prompt = """
            Extract search filters from this user query: "%s"

            Available genres: DRAMA, COMEDY, MUSICAL, OPERA, BALLET, THRILLER, CHILDREN

            Rules for each field (use null when the query does not clearly imply it):

            - titleKeyword: a single keyword likely to appear in a show title
              (e.g. "hamlet" for "show me hamlet"). Use null if the user is
              describing a mood rather than naming a show.
            - genre: EXACTLY one value from the list above, chosen from the
            - maxDurationMinutes: integer cap in minutes.
                "short"           -> 120
                "very short"      -> 90
                "under two hours" -> 120
              Use null if duration is not mentioned.
            """.formatted(naturalLanguageQuery);

        return chatClient.prompt()
            .user(prompt)
            .call()
            .entity(SearchFiltersResponse.class);
    }

    public ShowComparisonResponse compareShows(Long showId1, Long showId2, String occasion) {
        ShowResponse show1 = showService.getShowById(showId1);
        ShowResponse show2 = showService.getShowById(showId2);

        return chatClient.prompt()
            .user("Compare these two theatre shows for a " + occasion + ":\n\n"
                + "Show 1: " + show1.title() + " (" + show1.genre()
                + ", " + show1.durationMinutes() + " min)\n"
                + "Show 2: " + show2.title() + " (" + show2.genre()
                + ", " + show2.durationMinutes() + " min)\n\n"
                + "Which show is better for this occasion and why?")
            .call()
            .entity(ShowComparisonResponse.class);
    }
}
