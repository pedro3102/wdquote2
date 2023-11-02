package com.sapwd.wdquote.domain;

import com.sapwd.wdquote.domain.enumeration.OperationCounterpart;
import com.sapwd.wdquote.domain.enumeration.OperationType;
import com.sapwd.wdquote.domain.enumeration.converter.OperationCounterpartConverter;
import com.sapwd.wdquote.domain.enumeration.converter.OperationTypeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import java.io.Serializable;

/**
 * A MovementType.
 */
@Entity
@Table(name = "movement_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovementType extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 3)
    @Column(name = "code", length = 3, nullable = false)
    private String code;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Formula(value = "concat(code, ' - ', name)")
    private String codeName;

    @NotNull
    @Convert(converter = OperationTypeConverter.class)
    @Column(name = "type", nullable = false)
    private OperationType type;

    @Convert(converter = OperationCounterpartConverter.class)
    @Column(name = "counterpart", nullable = false)
    private OperationCounterpart counterpart;

    @JoinColumn(name = "opposite_movement_type_id", referencedColumnName = "id")
    @ManyToOne
    private MovementType oppositeMovementType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovementType id(Long id) {
        this.setId(id);
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MovementType code(String code) {
        this.setCode(code);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MovementType name(String name) {
        this.setName(name);
        return this;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public OperationType getType() {
        return this.type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public MovementType type(OperationType type) {
        this.setType(type);
        return this;
    }

    public OperationCounterpart getCounterpart() {
        return this.counterpart;
    }

    public void setCounterpart(OperationCounterpart counterpart) {
        this.counterpart = counterpart;
    }

    public MovementType counterpart(OperationCounterpart counterpart) {
        this.setCounterpart(counterpart);
        return this;
    }

    public MovementType getOppositeMovementType() {
        return oppositeMovementType;
    }

    public void setOppositeMovementType(MovementType oppositeMovementType) {
        this.oppositeMovementType = oppositeMovementType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovementType)) {
            return false;
        }
        return id != null && id.equals(((MovementType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovementType{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", counterpart='" + getCounterpart() + "'" +
            "}";
    }
}
