package se.inera.fmu.core.application.impl;

import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import se.inera.fmu.core.application.FmuOrderingService;
import se.inera.fmu.core.domain.model.eavrop.*;
import se.inera.fmu.core.domain.model.patient.*;

import javax.inject.Inject;
import java.util.HashMap;

/**
 * Created by Rasheed on 7/7/14.
 */

@Service
@Validated
public class FmuOrderingServiceImpl extends AbstractServiceImpl implements FmuOrderingService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final EavropRepository eavropRepository;
    private final PatientRepository patientRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    public FmuOrderingServiceImpl(final EavropRepository eavropRepository, final PatientRepository patientRepository) {
        this.eavropRepository = eavropRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public ÄrendeId createNewEavrop(ÄrendeId ärendeId, UtredningType utredningType, String tolk, String personalNumber,
                                    Name patientName, Gender patientGender, Address patientHomeAddress,
                                    String patientEmail) {
        Patient patient = new Patient(personalNumber, patientName, patientGender, patientHomeAddress, patientEmail);
        patient = patientRepository.save(patient);
        log.debug(String.format("patient created :: %s", patient.toString()));

        Eavrop eavrop = new Eavrop(ärendeId, utredningType, tolk, patient);
        eavrop = eavropRepository.save(eavrop);
        log.debug(String.format("eavrop created :: %s", eavrop));

        String businessKey = eavrop.getÄrendeId().toString();

        // Also set the eavrop as process-variable
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put("eavrop", eavrop);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("fmuProcess", businessKey, variables);
        log.debug(String.format("business process started :: %s", processInstance.getId()));

        return eavrop.getÄrendeId();
    }

}
