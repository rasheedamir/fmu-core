package se.inera.fmu.core.application.util;

import se.inera.fmu.core.domain.model.eavrop.Eavrop;
import se.inera.fmu.core.domain.model.eavrop.EavropId;

/**
 * Created by Rasheed on 7/8/14.
 */
public class EavropUtil {

    public static final String strEavropId = "12345";

    private EavropUtil() {

    }

    public static Eavrop createEavrop() {
        return new Eavrop(strEavropId);
    }
}
