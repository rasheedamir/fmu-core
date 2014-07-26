package se.inera.fmu.core.domain.shared;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Base Entity
 *
 * Created by Rasheed on 7/17/14.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //~ Instance fields ================================================================================================

    @Version
    @Column(name = "version")
    private Long version = 0L;

    //~ Property Methods ===============================================================================================

    public Long getVersion() {
        return version;
    }

    private void setVersion(final Long version){
        this.version = version;
    }

    //~ Common Methods =================================================================================================
}