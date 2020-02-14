package hu.dhajdukis.gridsandtiles;

import java.util.Optional;

import hu.dhajdukis.gridsandtiles.dto.DimensionsDto;
import hu.dhajdukis.gridsandtiles.dto.GridDto;
import hu.dhajdukis.gridsandtiles.entity.Grid;
import hu.dhajdukis.gridsandtiles.repository.GridRepository;
import hu.dhajdukis.gridsandtiles.service.GridService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GridServiceTest {

    private GridService gridService;

    @Mock
    GridRepository gridRepository;

    @BeforeEach
    void setup() {
        gridService = new GridService(gridRepository);
    }

    @Test
    void retrieveGridById() {
        final GridDto expectedGridDto = new GridDto();
        expectedGridDto.setName("name");
        final DimensionsDto dimensionsDto = new DimensionsDto();
        dimensionsDto.setHeight(3);
        dimensionsDto.setWidth(4);
        expectedGridDto.setDimensions(dimensionsDto);

        final Grid grid = new Grid();
        grid.setId(1L);
        grid.setName("name");
        grid.setHeight(3);
        grid.setWidth(4);

        final Optional<Grid> optionalGrid = Optional.of(grid);

        when(gridRepository.findById(1L)).thenReturn(optionalGrid);

        final GridDto resultGridDto = gridService.retrieveGridById(1L);

        Assertions.assertEquals(resultGridDto.getName(), expectedGridDto.getName());
        Assertions.assertEquals(resultGridDto.getDimensions().getHeight(),
                expectedGridDto.getDimensions().getHeight());
        Assertions.assertEquals(resultGridDto.getDimensions().getWidth(),
                expectedGridDto.getDimensions().getWidth());
    }

    @Test
    void createGridMissingDimension() {
        final GridDto gridDto = new GridDto();
        gridDto.setName("name");

        final Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> gridService.createGrid(gridDto));
        Assertions.assertEquals("Dimension parameters should not be null", exception.getMessage());
    }

    @Test
    void createGridNonPositveDimension() {
        final GridDto gridDto = new GridDto();
        gridDto.setName("name");

        final DimensionsDto dimensionsDto = new DimensionsDto();
        dimensionsDto.setHeight(-1);
        dimensionsDto.setWidth(0);

        gridDto.setDimensions(dimensionsDto);

        final Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> gridService.createGrid(gridDto));
        Assertions.assertEquals("Height: -1 and/or width: 0 Both should be minimum 2", exception.getMessage());
    }

    @Test
    void createGrid() {
        final GridDto gridDto = new GridDto();
        gridDto.setName("name");

        final DimensionsDto dimensionsDto = new DimensionsDto();
        dimensionsDto.setHeight(11);
        dimensionsDto.setWidth(11);

        gridDto.setDimensions(dimensionsDto);

        final GridDto resultGridDto = gridService.createGrid(gridDto);

        Assertions.assertEquals(resultGridDto.getDimensions().getWidth(), gridDto.getDimensions().getWidth());
        Assertions.assertEquals(resultGridDto.getDimensions().getHeight(),
                gridDto.getDimensions().getHeight());
        Assertions.assertEquals(resultGridDto.getName(),
                gridDto.getName());
        Assertions.assertNull(resultGridDto.getTiles());
    }
}
