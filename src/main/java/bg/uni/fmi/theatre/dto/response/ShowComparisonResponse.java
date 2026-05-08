package bg.uni.fmi.theatre.dto.response;

public record ShowComparisonResponse(
    String show1Title,
    String show2Title,
    String recommendation,
    String reasoning
) {}