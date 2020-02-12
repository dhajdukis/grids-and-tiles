package hu.dhajdukis.gridsandtiles;

import java.util.Optional;

import hu.dhajdukis.gridsandtiles.dto.PositionDto;
import hu.dhajdukis.gridsandtiles.dto.TileDto;
import hu.dhajdukis.gridsandtiles.entity.Grid;
import hu.dhajdukis.gridsandtiles.entity.Tile;
import hu.dhajdukis.gridsandtiles.repository.GridRepository;
import hu.dhajdukis.gridsandtiles.repository.TileRepository;
import hu.dhajdukis.gridsandtiles.service.TileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TileServiceTest {

    private TileService tileService;

    @Mock
    GridRepository gridRepository;

    @Mock
    TileRepository tileRepository;

    @BeforeEach
    void setup() {
        tileService = new TileService(tileRepository, gridRepository);
    }

    @Test
    void retrieveTileById() {
        final TileDto expectedTileDto = new TileDto();
        expectedTileDto.setId(1L);
        expectedTileDto.setTitle("name");
        expectedTileDto.setUrl("http://hkd.com");
        expectedTileDto.setGridId(1L);
        final PositionDto positionDto = new PositionDto();
        positionDto.setXposition(3);
        positionDto.setYposition(4);
        expectedTileDto.setPosition(positionDto);

        final Tile tile = new Tile();
        tile.setId(1L);
        tile.setTitle("name");
        tile.setUrl("http://hkd.com");
        tile.setXposition(3);
        tile.setYposition(4);

        final Grid grid = new Grid();
        grid.setId(1L);

        tile.setGrid(grid);

        final Optional<Tile> optionalTile = Optional.of(tile);

        when(tileRepository.findById(1L)).thenReturn(optionalTile);

        Assertions.assertEquals(tileService.retrieveTileById(1L).getId(), expectedTileDto.getId());
        Assertions.assertEquals(tileService.retrieveTileById(1L).getTitle(), expectedTileDto.getTitle());
        Assertions.assertEquals(tileService.retrieveTileById(1L).getUrl(),
                expectedTileDto.getUrl());
        Assertions.assertEquals(tileService.retrieveTileById(1L).getPosition().getXposition(),
                expectedTileDto.getPosition().getXposition());
        Assertions.assertEquals(tileService.retrieveTileById(1L).getPosition().getYposition(),
                expectedTileDto.getPosition().getYposition());
        Assertions.assertEquals(tileService.retrieveTileById(1L).getGridId(),
                expectedTileDto.getGridId());
    }

    @Test
    void createTileSetId() {
        final TileDto tileDto = new TileDto();
        tileDto.setId(1L);

        final Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> tileService.createTile(tileDto));
        Assertions.assertEquals("Id must be empty", exception.getMessage());
    }

    @Test
    void createTilePositionIsNull() {
        final TileDto tileDto = new TileDto();

        final Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> tileService.createTile(tileDto));
        Assertions.assertEquals("Postion parameters should not be null", exception.getMessage());
    }

    @Test
    void createTileGridIdDoesNotExist() {
        final TileDto tileDto = new TileDto();
        final PositionDto positionDto = new PositionDto();
        positionDto.setXposition(3);
        positionDto.setYposition(4);
        tileDto.setPosition(positionDto);
        tileDto.setGridId(2L);

        when(gridRepository.findById(2L)).thenReturn(Optional.empty());

        final Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> tileService.createTile(tileDto));
        Assertions.assertEquals("Grid with the given id: 2 does not exist", exception.getMessage());
    }

    @Test
    void createTilePositionIsOutSideOfTheGrid() {
        final TileDto tileDto = new TileDto();
        final PositionDto positionDto = new PositionDto();
        positionDto.setXposition(10);
        positionDto.setYposition(9);
        tileDto.setPosition(positionDto);
        tileDto.setGridId(1L);

        final Grid grid = new Grid();
        grid.setId(1L);
        grid.setWidth(9);
        grid.setHeight(2);
        final Optional<Grid> optionalGrid = Optional.of(grid);

        when(gridRepository.findById(1L)).thenReturn(optionalGrid);

        final Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> tileService.createTile(tileDto));
        Assertions.assertEquals("Position (10x9) is outside of the grid's dimensions: 9x2", exception.getMessage());
    }

    @Test
    void createTilePositionIsTaken() {
        final TileDto tileDto = new TileDto();
        final PositionDto positionDto = new PositionDto();
        positionDto.setXposition(10);
        positionDto.setYposition(9);
        tileDto.setPosition(positionDto);
        tileDto.setGridId(1L);

        final Grid grid = new Grid();
        grid.setId(1L);
        grid.setWidth(10);
        grid.setHeight(10);
        final Optional<Grid> optionalGrid = Optional.of(grid);

        final Optional<Tile> optionalTile = Optional.of(new Tile());

        when(gridRepository.findById(1L)).thenReturn(optionalGrid);
        when(tileRepository.findByGridIdAndXpositionAndYposition(1L, 10, 9)).thenReturn(optionalTile);

        final Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> tileService.createTile(tileDto));
        Assertions.assertEquals("The position is taken", exception.getMessage());
    }

    @Test
    void createTile() {
        final TileDto tileDto = new TileDto();
        final PositionDto positionDto = new PositionDto();
        positionDto.setXposition(10);
        positionDto.setYposition(9);
        tileDto.setPosition(positionDto);
        tileDto.setGridId(1L);
        tileDto.setTitle("test");
        tileDto.setUrl("http://hkd.com");

        final Grid grid = new Grid();
        grid.setId(1L);
        grid.setWidth(10);
        grid.setHeight(10);
        final Optional<Grid> optionalGrid = Optional.of(grid);

        when(gridRepository.findById(1L)).thenReturn(optionalGrid);
        when(tileRepository.findByGridIdAndXpositionAndYposition(1L, 10, 9)).thenReturn(Optional.empty());

        final TileDto resultTileDto = tileService.createTile(tileDto);

        Assertions.assertEquals(resultTileDto.getTitle(), tileDto.getTitle());
        Assertions.assertEquals(resultTileDto.getUrl(), tileDto.getUrl());
        Assertions.assertEquals(resultTileDto.getPosition().getXposition(),
                tileDto.getPosition().getXposition());
        Assertions.assertEquals(resultTileDto.getPosition().getYposition(),
                tileDto.getPosition().getYposition());
    }

}
