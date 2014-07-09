package se.inera.fmu.core.domain.model.eavrop;

import org.apache.commons.lang3.Validate;
import se.inera.fmu.core.domain.shared.IValueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Rasheed on 7/7/14.
 *
 * Uniquely identifies a particular Eavrop.
 *
 */
@Embeddable
public final class EavropId implements IValueObject<EavropId> {

    //~ Instance fields ================================================================================================

    @Column(name = "eavrop_id", nullable = false, updatable = false)
    @NotNull
    @Size(max = 24)
    private String id;

    //~ Constructors ===================================================================================================

    EavropId() {
        // Needed by Hibernate
    }

    /**
     * Constructor.
     *
     * @param id Id string.
     */
    public EavropId(final String id) {
        Validate.notNull(id);
        this.id = id;
    }

    //~ Methods ========================================================================================================

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

    @Override
    public String toString() {
        return id;
    }

}
