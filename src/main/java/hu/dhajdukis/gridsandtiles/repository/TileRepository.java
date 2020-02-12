package hu.dhajdukis.gridsandtiles.repository;

import java.util.Optional;

import hu.dhajdukis.gridsandtiles.entity.Tile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TileRepository extends JpaRepository<Tile, Long> {
    Optional<Tile> findByGridIdAndXpositionAndYposition(
            final Long gridId,
            final Integer xposition,
            final Integer yposition
    );
}
