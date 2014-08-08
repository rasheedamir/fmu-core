package se.inera.fmu.core.application.util;

import se.inera.fmu.core.domain.model.patient.*;

/**
 * Created by Rasheed on 8/7/14.
 */
public final class PatientUtil {

    public static final String PERSONAL_NUMBER = "19900208-9009";
    public static final Name NAME = new Name(Initials.MR, "FRODO", "", "BAGINS");
    public static final Gender GENDER = Gender.MALE;
    public static final Address HOME_ADDRESS = new Address("HOME # 1", "STREET # 2", "8899", "CHADONE", "VEVEY", "SWITZERLAND");
    public static final String EMAIL = "frodo@bagins.com";

    public static Patient createPatient() {
        return new Patient(PERSONAL_NUMBER, NAME, GENDER, HOME_ADDRESS, EMAIL);
    }
}
