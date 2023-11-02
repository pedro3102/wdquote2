package com.sapwd.wdquote.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;

/**
 * Criteria class for the {@link com.sapwd.wdquote.domain.UnitOfMeasureConversion} entity. This class is used
 * in {@link com.sapwd.wdquote.web.rest.UnitOfMeasureConversionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vendors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UnitOfMeasureConversionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter uomId;

    private Boolean distinct;

    public UnitOfMeasureConversionCriteria() {}

    public UnitOfMeasureConversionCriteria(UnitOfMeasureConversionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uomId = other.uomId == null ? null : other.uomId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UnitOfMeasureConversionCriteria copy() {
        return new UnitOfMeasureConversionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUomId() {
        return uomId;
    }

    public LongFilter uomId() {
        if (uomId == null) {
            uomId = new LongFilter();
        }
        return uomId;
    }

    public void setUomId(LongFilter uomId) {
        this.uomId = uomId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UnitOfMeasureConversionCriteria that = (UnitOfMeasureConversionCriteria) o;
        return (Objects.equals(id, that.id) && Objects.equals(uomId, that.uomId) && Objects.equals(distinct, that.distinct));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uomId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitOfMeasureConversionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uomId != null ? "uomId=" + uomId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
