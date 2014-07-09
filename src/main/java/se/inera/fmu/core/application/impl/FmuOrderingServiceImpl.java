package se.inera.fmu.core.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import se.inera.fmu.core.application.FmuOrderingService;
import se.inera.fmu.core.domain.model.eavrop.Eavrop;
import se.inera.fmu.core.domain.model.eavrop.EavropId;
import se.inera.fmu.core.domain.model.eavrop.EavropRepository;

import javax.inject.Inject;

/**
 * Created by Rasheed on 7/7/14.
 */

@Service
@Validated
public class FmuOrderingServiceImpl implements FmuOrderingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FmuOrderingServiceImpl.class);
    private final EavropRepository eavropRepository;

    @Inject
    public FmuOrderingServiceImpl(final EavropRepository eavropRepository) {
        this.eavropRepository = eavropRepository;
    }

    @Override
    public Eavrop createNewEavrop(String strEavropId) {
        Eavrop eavrop = new Eavrop(strEavropId);
        return eavropRepository.save(eavrop);
    }
}
