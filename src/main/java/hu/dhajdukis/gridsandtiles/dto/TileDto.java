package hu.dhajdukis.gridsandtiles.dto;

import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class TileDto {
    private Long id;
    @Valid
    private PositionDto position;
    private String title;
    @URL
    private String url;
    private Long GridId;
}
