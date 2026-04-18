package bg.uni.fmi.theatre.dto;

import java.util.List;

public record ShowSummaryResponse(
    String summary,
    String targetAudience,
    List<String> highlights
) {}