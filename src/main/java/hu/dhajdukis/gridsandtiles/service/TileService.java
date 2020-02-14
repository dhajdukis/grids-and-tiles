package hu.dhajdukis.gridsandtiles.service;

import java.util.Optional;

import hu.dhajdukis.gridsandtiles.dto.PositionDto;
import hu.dhajdukis.gridsandtiles.dto.TileDto;
import hu.dhajdukis.gridsandtiles.entity.Grid;
import hu.dhajdukis.gridsandtiles.entity.Tile;
import hu.dhajdukis.gridsandtiles.repository.GridRepository;
import hu.dhajdukis.gridsandtiles.repository.TileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TileService extends AbstractService {
    private final TileRepository tileRepository;
    private final GridRepository gridRepository;

    public TileService(
            final TileRepository tileRepository,
            final GridRepository gridRepository
    ) {
        this.tileRepository = tileRepository;
        this.gridRepository = gridRepository;
    }

    @Transactional(readOnly = true)
    public TileDto retrieveTileById(final Long tileid) {
        final Optional<Tile> tile = tileRepository.findById(tileid);
        return tile.isEmpty() ? null : createModelMapper().map(tile.get(), TileDto.class);
    }

    @Transactional
    public TileDto createTile(final Long gridId, final TileDto tileDto) {
        final PositionDto positionDto = tileDto.getPosition();

        checkIfIdIsSet(tileDto.getId());
        checkIfPositionIsNull(positionDto);
        checkPosition(positionDto);
        final Grid grid = checkGridId(gridId);
        checkIfPositionIsOutSideOfTheGrid(gridId, tileDto);
        checkIfPositionIsTaken(gridId, tileDto);

        final Tile tile = createModelMapper().map(tileDto, Tile.class);
        tile.setGrid(grid);
        tileRepository.save(tile);

        return createModelMapper().map(tile, TileDto.class);
    }

    @Transactional
    public void deleteTile(final Long gridId, final Long tileId) {
        final Tile tile = findTileById(tileId);
        checkGridId(gridId);
        checkIfTileOnTheGrid(gridId, tileId);
        tileRepository.delete(tile);
    }

    @Transactional
    public TileDto editTile(final Long gridId, final Long tileId, final TileDto tileDto) {
        checkIfPositionIsSet(tileDto.getPosition());
        checkGridId(gridId);
        checkIfTileOnTheGrid(gridId, tileId);

        final Tile tileToEdit = findTileById(tileId);
        tileToEdit.setTitle(tileDto.getTitle());
        tileToEdit.setUrl(tileDto.getUrl());

        return createModelMapper().map(tileToEdit, TileDto.class);
    }

    private void checkIfTileOnTheGrid(final long gridId, final long tileId) {
        final Optional<Tile> tileOptional = tileRepository.findById(tileId);
        if (tileOptional.isEmpty()) {
            return;
        }
        final Tile tile = tileOptional.get();
        final Long originalGridId = tile.getGrid().getId();
        if (originalGridId != gridId) {
            throw new IllegalArgumentException(String.format("Tile's grid id: %d and the given grid id: %d is different",
                    originalGridId, tileId));
        }
    }

    private void checkIfPositionIsSet(final PositionDto positionDto) {
        if (positionDto != null) {
            throw new IllegalArgumentException("Position must not be set");
        }
    }

    private Tile findTileById(final Long tileId) {
        final Optional<Tile> tile = tileRepository.findById(tileId);
        if (tile.isEmpty()) {
            throw new IllegalArgumentException(String.format("Tile with the given id: %d does not exist", tileId));
        }
        return tile.get();
    }

    private void checkIfPositionIsOutSideOfTheGrid(final long gridId, final TileDto tileDto) {
        final Optional<Grid> grid = gridRepository.findById(gridId);
        final Integer xPosition = tileDto.getPosition().getXposition();
        final Integer yPosition = tileDto.getPosition().getYposition();
        final Integer width = grid.get().getWidth();
        final Integer height = grid.get().getHeight();

        if (width < xPosition || height < yPosition) {
            throw new IllegalArgumentException(String.format("Position (%dx%d) is outside of the grid's dimensions: %dx%d",
                    xPosition, yPosition, width, height));
        }
    }

    private void checkIfPositionIsTaken(final long gridId, final TileDto tileDto) {
        final Integer xPosition = tileDto.getPosition().getXposition();
        final Integer yPosition = tileDto.getPosition().getYposition();
        final Optional<Tile> tile = tileRepository.findByGridIdAndXpositionAndYposition(gridId, xPosition, yPosition);

        if (tile.isPresent()) {
            throw new IllegalArgumentException("The position is taken");
        }
    }

    private Grid checkGridId(final Long gridId) {
        final Optional<Grid> grid = gridRepository.findById(gridId);

        if (grid.isEmpty()) {
            throw new IllegalArgumentException(String.format("Grid with the given id: %d does not exist", gridId));
        }
        return grid.get();
    }

    private void checkIfPositionIsNull(final PositionDto positionDto) {
        if (positionDto == null || (positionDto.getXposition() == null || positionDto.getYposition() == null)) {
            throw new IllegalArgumentException("Postion parameters should not be null");
        }
    }

    private void checkPosition(final PositionDto positionDto) {
        final Integer xPosition = positionDto.getXposition();
        final Integer yPosition = positionDto.getYposition();

        if (xPosition < 1 || yPosition < 1) {
            throw new IllegalArgumentException(String.format("X: %d and/or Y: %d position should be positive number(s)",
                    xPosition, yPosition));
        }
    }

    private void checkIfIdIsSet(final Long id) {
        if (id != null) {
            throw new IllegalArgumentException("Id must be empty");
        }
    }
}
