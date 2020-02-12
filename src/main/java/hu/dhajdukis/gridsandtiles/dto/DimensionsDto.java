package hu.dhajdukis.gridsandtiles.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DimensionsDto {
    @NotNull(message = "Please provide width")
    @Min(value = 2, message = "Width should be more than 1")
    private Integer width;
    @NotNull(message = "Please provide height")
    @Min(value = 2, message = "Height should be more than 1")
    private Integer height;
}
