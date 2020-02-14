package hu.dhajdukis.gridsandtiles.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GridDto {
    @Valid
    private DimensionsDto dimensions;
    @NotNull(message = "Please provide name")
    private String name;
    @Valid
    private List<TileDto> tiles;
}
