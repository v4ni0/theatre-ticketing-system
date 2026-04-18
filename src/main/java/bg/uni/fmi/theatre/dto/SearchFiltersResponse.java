package bg.uni.fmi.theatre.dto;

public record SearchFiltersResponse(
    String titleKeyword,
    String genre,
    Integer maxDurationMinutes
) {}