package bg.uni.fmi.theatre.domain;

public record Hall(long id, String name) {

    public Hall {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Hall name is required");
        }
    }

    @Override
    public String toString() {
        return "Hall{id=%d, name='%s'}".formatted(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hall h)) return false;
        return id == h.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
