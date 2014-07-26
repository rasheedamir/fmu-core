package se.inera.fmu.core.domain.model.patient;

import org.hibernate.validator.constraints.Email;
import se.inera.fmu.core.domain.model.eavrop.Eavrop;
import se.inera.fmu.core.domain.shared.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Created by Rasheed on 7/23/14.
 */
@Entity
@Table(name = "T_PATIENT")
public class Patient extends BaseEntityAudit implements IEntity<Patient> {

    //~ Instance fields ================================================================================================

    @NotNull
    @Embedded
    private PatientId patientId;

    @Column(name = "initials", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Initials initials;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Size(min = 0, max = 50)
    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Embedded
    private Address homeAddress;

    @Email
    @Column(name = "email")
    private String email;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "patient")
    private Set<Eavrop> eavrops;

    //~ Constructors ===================================================================================================

    Patient() {
        // Needed by Hibernate
    }

    //~ Property Methods ===============================================================================================

    public Initials getInitials() {
        return initials;
    }

    public void setInitials(Initials initials) {
        this.initials = initials;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Eavrop> getEavrops() {
        return eavrops;
    }

    public void setEavrops(Set<Eavrop> eavrops) {
        this.eavrops = eavrops;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    //~ Other Methods ==================================================================================================

    @Override
    public boolean sameIdentityAs(final Patient other) {
        return other != null && getPatientId().equals(other.getPatientId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        if (!patientId.equals(patient.patientId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return patientId.hashCode();
    }
}