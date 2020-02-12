package hu.dhajdukis.gridsandtiles.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public abstract class AbstractService {
    protected ModelMapper createModelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }

    protected void checkIfIdIsSet(final Long id) {
        if (id != null) {
            throw new IllegalArgumentException("Id must be empty");
        }
    }
}
