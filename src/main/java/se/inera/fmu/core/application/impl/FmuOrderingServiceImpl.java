package se.inera.fmu.core.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import se.inera.fmu.core.application.FmuOrderingService;
import se.inera.fmu.core.domain.model.eavrop.Eavrop;
import se.inera.fmu.core.domain.model.eavrop.EavropRepository;
import se.inera.fmu.core.domain.model.eavrop.UtredningType;
import se.inera.fmu.core.domain.model.eavrop.ÄrendeId;
import se.inera.fmu.core.domain.model.patient.Patient;

import javax.inject.Inject;

/**
 * Created by Rasheed on 7/7/14.
 */

@Service
@Validated
public class FmuOrderingServiceImpl implements FmuOrderingService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final EavropRepository eavropRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    public FmuOrderingServiceImpl(final EavropRepository eavropRepository) {
        this.eavropRepository = eavropRepository;
    }

    @Override
    public ÄrendeId createNewEavrop(String strEavropId, UtredningType utredningType, String tolk, Patient patient) {
        Eavrop eavrop = new Eavrop(strEavropId, utredningType, tolk, patient);
        eavrop = eavropRepository.save(eavrop);
        return eavrop.getÄrendeId();
    }
}
