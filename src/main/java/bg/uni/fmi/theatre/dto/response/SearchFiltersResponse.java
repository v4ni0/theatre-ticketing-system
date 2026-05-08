package bg.uni.fmi.theatre.dto.response;

public record SearchFiltersResponse(
    String titleKeyword,
    String genre,
    Integer maxDurationMinutes
) {}