package se.inera.fmu.core.application;

import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.fmu.core.application.impl.FmuOrderingServiceImpl;
import se.inera.fmu.core.application.util.EavropUtil;
import se.inera.fmu.core.application.util.PatientUtil;
import se.inera.fmu.core.domain.model.eavrop.Eavrop;
import se.inera.fmu.core.domain.model.eavrop.EavropId;
import se.inera.fmu.core.domain.model.eavrop.EavropRepository;
import se.inera.fmu.core.domain.model.eavrop.ÄrendeId;
import se.inera.fmu.core.domain.model.patient.Patient;
import se.inera.fmu.core.domain.model.patient.PatientRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Rasheed on 7/7/14.
 */
@RunWith(MockitoJUnitRunner.class)
public class FmuOrderingServiceImplTest {

    @Mock
    private EavropRepository eavropRepository;

    @Mock
    private PatientRepository patientRepository;

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    private FmuOrderingServiceImpl fmuOrderingService;

    @Before
    public void setUp() {
        fmuOrderingService = new FmuOrderingServiceImpl(eavropRepository, patientRepository);
        fmuOrderingService.setRuntimeService(activitiRule.getRuntimeService());
        fmuOrderingService.setIdentityService(activitiRule.getIdentityService());
        fmuOrderingService.setTaskService(activitiRule.getTaskService());
    }

    @Test
    @Deployment(resources = {"processes/fmu.bpmn"})
    public void shouldCreateNewEavrop() {
        final Eavrop savedEavrop = stubRepositoryToReturnEavropOnSave();
        final Patient savedPatient = stubRepositoryToReturnPatientOnSave();
        final ÄrendeId ärendeId = fmuOrderingService.createNewEavrop(EavropUtil.ÄRENDE_ID, EavropUtil.UTREDNING_TYPE,
                                                                         EavropUtil.TOLK, PatientUtil.PERSONAL_NUMBER,
                                                                         PatientUtil.NAME, PatientUtil.GENDER,
                                                                         PatientUtil.HOME_ADDRESS, PatientUtil.EMAIL);
        // verify repository's were called
        verify(eavropRepository, times(1)).save(savedEavrop);
        verify(patientRepository, times(1)).save(savedPatient);
        assertEquals("Returned ÄrendeId should come from the repository", savedEavrop.getÄrendeId(), ärendeId);
    }

    private Eavrop stubRepositoryToReturnEavropOnSave() {
        Eavrop eavrop = EavropUtil.createEavrop();
        when(eavropRepository.save(any(Eavrop.class))).thenReturn(eavrop);
        return eavrop;
    }

    private Patient stubRepositoryToReturnPatientOnSave() {
        Patient patient = PatientUtil.createPatient();
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        return patient;
    }
}
