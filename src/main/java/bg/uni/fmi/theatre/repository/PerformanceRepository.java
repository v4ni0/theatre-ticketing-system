package bg.uni.fmi.theatre.repository;

import bg.uni.fmi.theatre.domain.Performance;
import bg.uni.fmi.theatre.domain.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findByShow(Show show);

    List<Performance> findByShowId(Long showId);
}