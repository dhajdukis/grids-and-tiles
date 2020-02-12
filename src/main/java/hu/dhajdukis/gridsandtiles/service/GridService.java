package hu.dhajdukis.gridsandtiles.service;

import java.util.Optional;

import hu.dhajdukis.gridsandtiles.dto.DimensionsDto;
import hu.dhajdukis.gridsandtiles.dto.GridDto;
import hu.dhajdukis.gridsandtiles.entity.Grid;
import hu.dhajdukis.gridsandtiles.repository.GridRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GridService extends AbstractService {
    final GridRepository gridRepository;

    public GridService(final GridRepository gridRepository) {
        this.gridRepository = gridRepository;
    }

    @Transactional(readOnly = true)
    public GridDto retrieveGridById(final Long id) {
        final Optional<Grid> grid = gridRepository.findById(id);
        return grid.isEmpty() ? null : createModelMapper().map(grid.get(), GridDto.class);
    }

    @Transactional
    public GridDto createGrid(final GridDto gridDto) {
        checkIfIdIsSet(gridDto.getId());
        checkIfDimensionIsNull(gridDto.getDimensions());
        checkDimensions(gridDto.getDimensions());
        final Grid grid = createModelMapper().map(gridDto, Grid.class);
        gridRepository.save(grid);
        return createModelMapper().map(grid, GridDto.class);
    }

    private void checkIfDimensionIsNull(final DimensionsDto dimensionsDto) {
        if (dimensionsDto == null || (dimensionsDto.getWidth() == null || dimensionsDto.getHeight() == null)) {
            throw new IllegalArgumentException("Dimension parameters should not be null");
        }
    }

    private void checkDimensions(final DimensionsDto dimensionsDto) {
        final Integer height = dimensionsDto.getHeight();
        final Integer width = dimensionsDto.getWidth();

        if (height < 2 || width < 2) {
            throw new IllegalArgumentException(String.format("Height: %d and/or width: %d Both should be minimum 2", height, width));
        }
    }
}
