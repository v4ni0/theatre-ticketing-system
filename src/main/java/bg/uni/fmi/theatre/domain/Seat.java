package bg.uni.fmi.theatre.domain;

import java.util.Objects;

public record Seat(Long id, Long hallId, String rowLabel, int seatNumber, String zoneCode) {

    public Seat {
        if (hallId == null) throw new IllegalArgumentException("hallId is required");
        if (rowLabel == null || rowLabel.isBlank()) throw new IllegalArgumentException("rowLabel is required");
        if (seatNumber <= 0) throw new IllegalArgumentException("seatNumber must be positive");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat s)) return false;
        return Objects.equals(id, s.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Seat{id=" + id + ", hall=" + hallId + ", row=" + rowLabel + seatNumber + ", zone=" + zoneCode + "}";
    }
}