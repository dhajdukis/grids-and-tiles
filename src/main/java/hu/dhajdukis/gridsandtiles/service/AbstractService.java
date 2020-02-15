package hu.dhajdukis.gridsandtiles.service;

import hu.dhajdukis.gridsandtiles.repository.GridRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public abstract class AbstractService {
    final GridRepository gridRepository;

    protected AbstractService(final GridRepository gridRepository) {
        this.gridRepository = gridRepository;
    }

    protected ModelMapper createModelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }
}
