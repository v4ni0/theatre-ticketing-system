package bg.uni.fmi.theatre.service;

import bg.uni.fmi.theatre.domain.AgeRating;
import bg.uni.fmi.theatre.domain.Genre;
import bg.uni.fmi.theatre.repository.ShowRepository;
import bg.uni.fmi.theatre.repository.inmemory.InMemoryShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShowServiceTest {
    private ShowService showService;
    private ShowRepository inMemoryShowRepository;

    @BeforeEach
    public void setUp() {
        inMemoryShowRepository = new InMemoryShowRepository();
        showService = new ShowService(inMemoryShowRepository);
    }
}