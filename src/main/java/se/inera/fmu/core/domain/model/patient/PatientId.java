package se.inera.fmu.core.domain.model.patient;

import lombok.ToString;
import se.inera.fmu.core.domain.shared.ValueObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Rasheed on 7/26/14.
 *
 */
@ToString
@Embeddable
public final class PatientId implements ValueObject<PatientId> {

    //~ Instance fields ================================================================================================

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "patient_id", updatable = false, nullable = false)
    private Long id;

    //~ Constructors ===================================================================================================

    public PatientId(Long id) {
        this.setId(id);
    }

    //~ Property Methods ===============================================================================================

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    //~ Other fields ===================================================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientId other = (PatientId) o;

        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean sameValueAs(PatientId other) {
        return other != null && this.id.equals(other.id);
    }
}
