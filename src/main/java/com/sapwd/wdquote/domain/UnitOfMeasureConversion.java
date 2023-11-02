package com.sapwd.wdquote.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UnitOfMeasureConversion.
 */
@Entity
@Table(name = "unit_of_measure_conversion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitOfMeasureConversion extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "conversion_factor", precision = 21, scale = 2, nullable = false)
    private BigDecimal conversionFactor;

    @ManyToOne(optional = false)
    @NotNull
    private UnitOfMeasure uom;

    @ManyToOne(optional = false)
    @NotNull
    private UnitOfMeasure uomEquivalent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UnitOfMeasureConversion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getConversionFactor() {
        return this.conversionFactor;
    }

    public UnitOfMeasureConversion conversionFactor(BigDecimal conversionFactor) {
        this.setConversionFactor(conversionFactor);
        return this;
    }

    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public UnitOfMeasure getUom() {
        return this.uom;
    }

    public void setUom(UnitOfMeasure unitOfMeasure) {
        this.uom = unitOfMeasure;
    }

    public UnitOfMeasureConversion uom(UnitOfMeasure unitOfMeasure) {
        this.setUom(unitOfMeasure);
        return this;
    }

    public UnitOfMeasure getUomEquivalent() {
        return this.uomEquivalent;
    }

    public void setUomEquivalent(UnitOfMeasure unitOfMeasure) {
        this.uomEquivalent = unitOfMeasure;
    }

    public UnitOfMeasureConversion uomEquivalent(UnitOfMeasure unitOfMeasure) {
        this.setUomEquivalent(unitOfMeasure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitOfMeasureConversion)) {
            return false;
        }
        return id != null && id.equals(((UnitOfMeasureConversion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitOfMeasureConversion{" +
            "id=" + getId() +
            ", conversionFactor=" + getConversionFactor() +
            "}";
    }
}
