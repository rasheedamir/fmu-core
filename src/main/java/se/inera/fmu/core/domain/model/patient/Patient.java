package se.inera.fmu.core.domain.model.patient;

import lombok.ToString;
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
@ToString
public class Patient extends BaseEntityAudit implements IEntity<Patient> {

    //~ Instance fields ================================================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "patient_id", updatable = false, nullable = false)
    private Long patientId;

    @NotNull
    @Embedded
    private Name name;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

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

    public Patient(Name name, Gender gender, Address homeAddress, String email) {
        this.setName(name);
        this.setGender(gender);
        this.setHomeAddress(homeAddress);
        this.setEmail(email);
    }

    //~ Property Methods ===============================================================================================

    public Name getName() {
        return name;
    }

    private void setName(Name name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    private void setGender(Gender gender) {
        this.gender = gender;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    private void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public Set<Eavrop> getEavrops() {
        return eavrops;
    }

    public PatientId getPatientId() {
        return new PatientId(this.patientId);
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
