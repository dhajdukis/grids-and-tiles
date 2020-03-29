package hu.dhajdukis.gridsandtiles.rest;

import javax.validation.Valid;

import hu.dhajdukis.gridsandtiles.dto.GridDto;
import hu.dhajdukis.gridsandtiles.dto.TileDto;
import hu.dhajdukis.gridsandtiles.service.GridService;
import hu.dhajdukis.gridsandtiles.service.TileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/grids")
public class GridController {
    private final GridService gridService;
    private final TileService tileService;

    public GridController(final GridService gridService, final TileService tileService) {
        this.gridService = gridService;
        this.tileService = tileService;
    }

    @GetMapping(value = "/{gridId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GridDto grid(@PathVariable("gridId") final Long id) {
        return gridService.retrieveGridById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GridDto createGrid(@Valid @RequestBody final GridDto gridDto) {
        try {
            return gridService.createGrid(gridDto);
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/{gridId}/tiles/{tileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TileDto tile(@PathVariable("gridId") final Long gridId, @PathVariable("tileId") final Long tileId) {
        return tileService.retrieveTileById(tileId);
    }

    @PostMapping(value = "/{gridId}/tiles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TileDto createTile(@PathVariable("gridId") final Long gridId, @Valid @RequestBody final TileDto tileDto) {
        try {
            return tileService.createTile(gridId, tileDto);
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{gridId}/tiles/{tileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTile(@PathVariable("gridId") final Long gridId, @PathVariable("tileId") final Long tileId) {
        try {
            tileService.deleteTile(gridId, tileId);
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping(value = "/{gridId}/tiles/{tileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TileDto editTile(
            @PathVariable("gridId") final Long gridId, @PathVariable("tileId") final Long tileId,
            @Valid @RequestBody final TileDto tileDto
    ) {
        try {
            return tileService.editTile(gridId, tileId, tileDto);
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
