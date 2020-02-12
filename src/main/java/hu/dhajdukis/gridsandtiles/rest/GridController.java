package hu.dhajdukis.gridsandtiles.rest;

import javax.validation.Valid;

import hu.dhajdukis.gridsandtiles.dto.GridDto;
import hu.dhajdukis.gridsandtiles.service.GridService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/grid")
public class GridController {
    private final GridService gridService;

    public GridController(final GridService gridService) {
        this.gridService = gridService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GridDto grid(@PathVariable("id") final Long id) {
        return gridService.retrieveGridById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GridDto tile(@Valid @RequestBody final GridDto gridDto) {
        try {
            return gridService.createGrid(gridDto);
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
