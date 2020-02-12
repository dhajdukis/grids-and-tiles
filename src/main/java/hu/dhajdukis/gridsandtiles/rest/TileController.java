package hu.dhajdukis.gridsandtiles.rest;

import javax.validation.Valid;

import hu.dhajdukis.gridsandtiles.dto.TileDto;
import hu.dhajdukis.gridsandtiles.service.TileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/tile")
public class TileController {
    private final TileService tileService;

    public TileController(final TileService tileService) {
        this.tileService = tileService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TileDto tile(@PathVariable("id") final Long id) {
        return tileService.retrieveTileById(id);
    }

    @PutMapping(value = "/createTile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TileDto createTile(@Valid @RequestBody final TileDto tileDto) {
        try {
            return tileService.createTile(tileDto);
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTile(@PathVariable("id") final Long id) {
        try {
            tileService.deleteTile(id);
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @PutMapping(value = "/editTile")
    public TileDto editTile(@Valid @RequestBody final TileDto tileDto) {
        try {
            return tileService.editTile(tileDto);
        } catch (final IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
