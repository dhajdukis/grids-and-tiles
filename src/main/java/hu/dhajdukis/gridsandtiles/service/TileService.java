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
    public TileDto retrieveTileById(final Long id) {
        final Optional<Tile> tile = tileRepository.findById(id);
        return tile.isEmpty() ? null : createModelMapper().map(tile.get(), TileDto.class);
    }

    @Transactional
    public TileDto createTile(final TileDto tileDto) {
        final PositionDto positionDto = tileDto.getPosition();

        checkIfIdIsSet(tileDto.getId());
        checkIfPositionIsNull(positionDto);
        checkPosition(positionDto);
        checkGridId(tileDto.getGridId());
        checkIfPositionIsOutSideOfTheGrid(tileDto);
        checkIfPositionIsTaken(tileDto);

        final Tile tile = createModelMapper().map(tileDto, Tile.class);
        tileRepository.save(tile);

        return createModelMapper().map(tile, TileDto.class);
    }

    @Transactional
    public void deleteTile(final Long tileId) {
        final Tile tile = findTileById(tileId);
        checkIfIdIsNotSet(tileId);
        tileRepository.delete(tile);
    }

    @Transactional
    public TileDto editTile(final TileDto tileDto) {
        final Long tileId = tileDto.getId();
        checkIfIdIsNotSet(tileId);
        checkIfPositionIsSet(tileDto.getPosition());
        checkIfGridIdIsSet(tileDto.getGridId());

        final Tile tileToEdit = findTileById(tileId);
        tileToEdit.setTitle(tileDto.getTitle());
        tileToEdit.setUrl(tileDto.getUrl());

        return createModelMapper().map(tileToEdit, TileDto.class);
    }

    private void checkIfPositionIsSet(final PositionDto positionDto) {
        if (positionDto != null) {
            throw new IllegalArgumentException("Position must not be set");
        }
    }

    private void checkIfIdIsNotSet(final Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be empty");
        }
    }

    private void checkIfGridIdIsSet(final Long gridId) {
        if (gridId != null) {
            throw new IllegalArgumentException("Grid id must not be set");
        }
    }

    private Tile findTileById(final Long tileId) {
        final Optional<Tile> tile = tileRepository.findById(tileId);
        if (tile.isEmpty()) {
            throw new IllegalArgumentException(String.format("Tile with the given id: %d does not exist", tileId));
        }
        return tile.get();
    }

    private void checkIfPositionIsOutSideOfTheGrid(final TileDto tileDto) {
        final Optional<Grid> grid = gridRepository.findById(tileDto.getGridId());
        final Integer xPosition = tileDto.getPosition().getXposition();
        final Integer yPosition = tileDto.getPosition().getYposition();
        final Integer width = grid.get().getWidth();
        final Integer height = grid.get().getHeight();

        if (width < xPosition || height < yPosition) {
            throw new IllegalArgumentException(String.format("Position (%dx%d) is outside of the grid's dimensions: %dx%d",
                    xPosition, yPosition, width, height));
        }
    }

    private void checkIfPositionIsTaken(final TileDto tileDto) {
        final Long gridId = tileDto.getGridId();
        final Integer xPosition = tileDto.getPosition().getXposition();
        final Integer yPosition = tileDto.getPosition().getYposition();
        final Optional<Tile> tile = tileRepository.findByGridIdAndXpositionAndYposition(gridId, xPosition, yPosition);

        if (tile.isPresent()) {
            throw new IllegalArgumentException("The position is taken");
        }
    }

    private void checkGridId(final Long gridId) {
        final Optional<Grid> grid = gridRepository.findById(gridId);

        if (grid.isEmpty()) {
            throw new IllegalArgumentException(String.format("Grid with the given id: %d does not exist", gridId));
        }
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
}
