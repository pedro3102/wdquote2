package com.sapwd.wdquote.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.UnitOfMeasureConversion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitOfMeasureConversionDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal conversionFactor;

    private UnitOfMeasureDTO uom;

    private UnitOfMeasureDTO uomEquivalent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public UnitOfMeasureDTO getUom() {
        return uom;
    }

    public void setUom(UnitOfMeasureDTO uom) {
        this.uom = uom;
    }

    public UnitOfMeasureDTO getUomEquivalent() {
        return uomEquivalent;
    }

    public void setUomEquivalent(UnitOfMeasureDTO uomEquivalent) {
        this.uomEquivalent = uomEquivalent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitOfMeasureConversionDTO)) {
            return false;
        }

        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO = (UnitOfMeasureConversionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, unitOfMeasureConversionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitOfMeasureConversionDTO{" +
            "id=" + getId() +
            ", conversionFactor=" + getConversionFactor() +
            ", uom=" + getUom() +
            ", uomEquivalent=" + getUomEquivalent() +
            "}";
    }
}
