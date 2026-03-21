package bg.uni.fmi.theatre.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public record Performance(Long id, Long showId, Long hallId, LocalDateTime startTime, PerformanceStatus status) {
    public Performance {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (showId == null) {
            throw new IllegalArgumentException("showId cannot be null");
        }
        if (hallId == null) {
            throw new IllegalArgumentException("hallId cannot be null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Performance)) {
            return false;
        }
        return Objects.equals(id, ((Performance) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override public String toString() {
        return "Performance{id=" + id + ", showId=" + showId + ", start=" + startTime + ", status=" + status + "}";
    }
}
