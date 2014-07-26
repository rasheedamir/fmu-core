package se.inera.fmu.core.domain.model.patient;

import lombok.ToString;
import se.inera.fmu.core.domain.shared.ValueObject;

/**
 * Created by Rasheed on 7/26/14.
 *
 */
@ToString
public final class PatientId implements ValueObject<PatientId> {

    //~ Instance fields ================================================================================================
    private Long id = 0L;

    //~ Constructors ===================================================================================================
    public PatientId(Long id) {
        this.id = id;
    }

    //~ Property Methods ===============================================================================================
    public Long getId() {
        return id;
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
