package bg.uni.fmi.theatre.domain;

public record Show(long id, String title, String description, Genre genre, int durationMinutes, AgeRating ageRating) {
    private static final int MAX_TITLE_LENGTH = 100;

    public Show {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Show title is required");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Show title must be at most 100 characters");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Show durationMinutes must be greater than 0");
        }
        if (genre == null) {
            throw new IllegalArgumentException("Show genre is required");
        }
        if (ageRating == null) {
            throw new IllegalArgumentException("Show ageRating is required");
        }
    }

    @Override
    public String toString() {
        return "Show{id=%d, title='%s', genre=%s, duration=%d min, ageRating=%s}"
            .formatted(id, title, genre, durationMinutes, ageRating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Show s)) return false;
        return id == s.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
