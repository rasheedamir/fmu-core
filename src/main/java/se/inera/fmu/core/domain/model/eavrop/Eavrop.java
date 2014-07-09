package se.inera.fmu.core.domain.model.eavrop;

import org.apache.commons.lang3.Validate;
import se.inera.fmu.core.domain.shared.IEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by Rasheed on 7/7/14.
 */
@Entity
public class Eavrop implements IEntity<Eavrop> {

    //~ Instance fields ================================================================================================

    // Auto-generated surrogate key
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @NotNull
    private Long id;

    @Embedded
    private EavropId eavropId;

    //~ Constructors ===================================================================================================

    /**
     *
     * @param strEavropId
     */
    public Eavrop(final String strEavropId) {
        Validate.notNull(strEavropId);
        this.eavropId = new EavropId(strEavropId);
    }

    Eavrop() {
        //Needed by hibernate
    }

    //~ Property Methods ===============================================================================================

    /**
     * The eavrop id is the identity of this entity, and is unique.
     *
     * @return Eavrop id.
     */
    public EavropId getEavropId() {
        return eavropId;
    }

    //~ Override Methods ===============================================================================================

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

    @Override
    public String toString() {
        return eavropId.toString();
    }
}
