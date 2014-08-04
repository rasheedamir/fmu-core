package se.inera.fmu.core.application;

import se.inera.fmu.core.domain.model.eavrop.EavropId;
import se.inera.fmu.core.domain.model.eavrop.UtredningType;
import se.inera.fmu.core.domain.model.eavrop.ÄrendeId;
import se.inera.fmu.core.domain.model.patient.Patient;

/**
 * Created by Rasheed on 7/7/14.
 */
public interface FmuOrderingService {

    /**
     * Registers a new Eavrop in the tracking system.
     *
     * @param eavropId
     * @return
     */
    ÄrendeId createNewEavrop(String eavropId, UtredningType utredningType, String tolk, Patient patient);

}
