package se.inera.fmu.core.application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.fmu.core.application.impl.FmuOrderingServiceImpl;
import se.inera.fmu.core.application.util.EavropUtil;
import se.inera.fmu.core.domain.model.eavrop.Eavrop;
import se.inera.fmu.core.domain.model.eavrop.EavropId;
import se.inera.fmu.core.domain.model.eavrop.EavropRepository;

import static org.junit.Assert.*;
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

    private FmuOrderingService fmuOrderingService;

    @Before
    public void setUp() {
        fmuOrderingService = new FmuOrderingServiceImpl(eavropRepository);
    }

    @Test
    public void shouldCreateNewEavrop() {
        final Eavrop savedEavrop = stubRepositoryToReturnEavropOnSave();
        final Eavrop returnedEavrop = fmuOrderingService.createNewEavrop(EavropUtil.strEavropId);
        // verify repository was called with user
        verify(eavropRepository, times(1)).save(returnedEavrop);
        assertEquals("Returned user should come from the repository", savedEavrop, returnedEavrop);
    }

    private Eavrop stubRepositoryToReturnEavropOnSave() {
        Eavrop eavrop = EavropUtil.createEavrop();
        when(eavropRepository.save(any(Eavrop.class))).thenReturn(eavrop);
        return eavrop;
    }
}
