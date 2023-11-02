package com.sapwd.wdquote.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.UnitOfMeasure} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitOfMeasureDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 10)
    private String abbreviation;

    @NotNull
    private Boolean allowsDecimal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Boolean getAllowsDecimal() {
        return allowsDecimal;
    }

    public void setAllowsDecimal(Boolean allowsDecimal) {
        this.allowsDecimal = allowsDecimal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitOfMeasureDTO)) {
            return false;
        }

        UnitOfMeasureDTO unitOfMeasureDTO = (UnitOfMeasureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, unitOfMeasureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitOfMeasureDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", abbreviation='" + getAbbreviation() + "'" +
            ", allowsDecimal='" + getAllowsDecimal() + "'" +
            "}";
    }
}
