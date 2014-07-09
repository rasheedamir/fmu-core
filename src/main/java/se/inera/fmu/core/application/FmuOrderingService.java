package se.inera.fmu.core.application;

import se.inera.fmu.core.domain.model.eavrop.Eavrop;

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
    Eavrop createNewEavrop(String eavropId);
}