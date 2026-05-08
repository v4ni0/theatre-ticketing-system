package bg.uni.fmi.theatre.dto.response;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements
) {
}
