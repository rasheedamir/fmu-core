package se.inera.fmu.core.domain.model.eavrop;

import lombok.ToString;
import org.apache.commons.lang3.Validate;
import se.inera.fmu.core.domain.model.patient.Patient;
import se.inera.fmu.core.domain.shared.BaseEntityAudit;
import se.inera.fmu.core.domain.shared.IEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Created by Rasheed on 7/7/14.
 */
@Entity
@Table(name = "T_EAVROP")
@ToString
public class Eavrop extends BaseEntityAudit implements IEntity<Eavrop> {

    //~ Instance fields ================================================================================================

    @NotNull
    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "eavrop_id", updatable = false, nullable = false)
    private EavropId eavropId;

    @NotNull
    @Embedded
    private ÄrendeId ärendeId;

    @Column(name = "utredning_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private UtredningType utredningType;

    @Column(name = "tolk", length = 50)
    @Max(50)
    private String tolk;

    @ManyToOne
    private Patient patient;

    //~ Constructors ===================================================================================================

    Eavrop() {
        //Needed by hibernate
    }

    /**
     *
     * @param ärendeId
     */
    public Eavrop(final String ärendeId, final UtredningType utredningType, final String tolk, final Patient patient) {
        Validate.notNull(ärendeId);
        setÄrendeId(new ÄrendeId(ärendeId));
        setUtredningType(utredningType);
        setTolk(tolk);
        setPatient(patient);
    }

    //~ Property Methods ===============================================================================================

    /**
     * The ärendeId is the identity of this entity, and is unique.
     *
     * @return ärendeId
     */
    public ÄrendeId getÄrendeId() {
        return ärendeId;
    }

    public UtredningType getUtredningType() {
        return utredningType;
    }

    public String getTolk() {
        return tolk;
    }

    public Patient getPatient() {
        return patient;
    }

    private void setTolk(final String tolk) {
        this.tolk = tolk;
    }

    private void setUtredningType(final UtredningType utredningType) {
        this.utredningType = utredningType;
    }

    private void setÄrendeId(ÄrendeId ärendeId) {
        this.ärendeId = ärendeId;
    }

    private void setPatient(Patient patient) {
        this.patient = patient;
    }

    public EavropId getEavropId() {
        return eavropId;
    }

    //~ Other Methods ==================================================================================================

    @Override
    public boolean sameIdentityAs(final Eavrop other) {
        return other != null && eavropId.sameValueAs(other.eavropId);
    }

    /**
     * @param object to compare
     * @return True if they have the same identity
     * @see #sameIdentityAs(Eavrop)
     */
    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        final Eavrop other = (Eavrop) object;
        return sameIdentityAs(other);
    }

    /**
     * @return Hash code of tracking id.
     */
    @Override
    public int hashCode() {
        return eavropId.hashCode();
    }
}