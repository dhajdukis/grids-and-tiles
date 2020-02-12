package hu.dhajdukis.gridsandtiles.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionDto {
    @NotNull(message = "Please provide xposition")
    @Min(value = 1, message = "xposition should be a positive number")
    private Integer xposition;
    @NotNull(message = "Please provide yposition")
    @Min(value = 1, message = "yposition should be a positive number")
    private Integer yposition;
}
