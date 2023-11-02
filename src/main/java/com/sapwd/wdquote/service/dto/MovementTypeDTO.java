package com.sapwd.wdquote.service.dto;

import com.sapwd.wdquote.domain.enumeration.OperationCounterpart;
import com.sapwd.wdquote.domain.enumeration.OperationType;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.MovementType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovementTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 3)
    private String code;

    @NotNull
    @Size(max = 50)
    private String name;

    private String codeName;

    @NotNull
    private OperationType type;

    private OperationCounterpart counterpart;

    private MovementTypeDTO oppositeMovementType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public OperationCounterpart getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(OperationCounterpart counterpart) {
        this.counterpart = counterpart;
    }

    public MovementTypeDTO getOppositeMovementType() {
        return oppositeMovementType;
    }

    public void setOppositeMovementType(MovementTypeDTO oppositeMovementType) {
        this.oppositeMovementType = oppositeMovementType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovementTypeDTO)) {
            return false;
        }

        MovementTypeDTO movementTypeDTO = (MovementTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, movementTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovementTypeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", counterpart='" + getCounterpart() + "'" +
            "}";
    }
}
