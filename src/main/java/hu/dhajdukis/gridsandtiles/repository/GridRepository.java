package hu.dhajdukis.gridsandtiles.repository;

import hu.dhajdukis.gridsandtiles.entity.Grid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GridRepository extends JpaRepository<Grid, Long> {
}
