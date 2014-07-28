package se.inera.fmu.core.domain.model.eavrop;

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
public final class EavropId implements ValueObject<EavropId> {

    //~ Instance fields ================================================================================================

    private Long id;

    //~ Constructors ===================================================================================================

    public EavropId(Long id) {
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

        EavropId other = (EavropId) o;

        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean sameValueAs(EavropId other) {
        return other != null && this.id.equals(other.id);
    }
}
