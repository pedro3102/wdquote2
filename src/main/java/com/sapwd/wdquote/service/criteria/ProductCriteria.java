package com.sapwd.wdquote.service.criteria;

import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.sapwd.wdquote.domain.Product} entity. This class is used
 * in {@link com.sapwd.wdquote.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter description;

    private StringFilter codeDescription;

    private BigDecimalFilter weight;

    private StringFilter picture;

    private LongFilter uomId;

    private StringFilter uom;

    private LongFilter uomWeightId;

    private StringFilter uomWeight;

    private LongFilter categoryId;

    private StringFilter category;

    private LongFilter systemModelId;

    private StringFilter systemModels;

    private Boolean distinct;

    private StringFilter global;

    public ProductCriteria() {
    }

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.codeDescription = other.codeDescription == null ? null : other.codeDescription.copy();
        this.weight = other.weight == null ? null : other.weight.copy();
        this.picture = other.picture == null ? null : other.picture.copy();
        this.uomId = other.uomId == null ? null : other.uomId.copy();
        this.uom = other.uom == null ? null : other.uom.copy();
        this.uomWeightId = other.uomWeightId == null ? null : other.uomWeightId.copy();
        this.uomWeight = other.uomWeight == null ? null : other.uomWeight.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.systemModelId = other.systemModelId == null ? null : other.systemModelId.copy();
        this.systemModels = other.systemModels == null ? null : other.systemModels.copy();
        this.distinct = other.distinct;
        this.global = other.global;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public StringFilter getCodeDescription() {
        return codeDescription;
    }

    public void setCodeDescription(StringFilter codeDescription) {
        this.codeDescription = codeDescription;
    }

    public StringFilter codeDescription() {
        if (codeDescription == null) {
            codeDescription = new StringFilter();
        }
        return codeDescription;
    }

    public BigDecimalFilter getWeight() {
        return weight;
    }

    public void setWeight(BigDecimalFilter weight) {
        this.weight = weight;
    }

    public BigDecimalFilter weight() {
        if (weight == null) {
            weight = new BigDecimalFilter();
        }
        return weight;
    }

    public StringFilter getPicture() {
        return picture;
    }

    public void setPicture(StringFilter picture) {
        this.picture = picture;
    }

    public StringFilter picture() {
        if (picture == null) {
            picture = new StringFilter();
        }
        return picture;
    }

    public LongFilter getUomId() {
        return uomId;
    }

    public void setUomId(LongFilter uomId) {
        this.uomId = uomId;
    }

    public LongFilter uomId() {
        if (uomId == null) {
            uomId = new LongFilter();
        }
        return uomId;
    }

    public LongFilter getUomWeightId() {
        return uomWeightId;
    }

    public void setUomWeightId(LongFilter uomWeightId) {
        this.uomWeightId = uomWeightId;
    }

    public LongFilter uomWeightId() {
        if (uomWeightId == null) {
            uomWeightId = new LongFilter();
        }
        return uomWeightId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public LongFilter getSystemModelId() {
        return systemModelId;
    }

    public void setSystemModelId(LongFilter systemModelId) {
        this.systemModelId = systemModelId;
    }

    public LongFilter systemModelId() {
        if (systemModelId == null) {
            systemModelId = new LongFilter();
        }
        return systemModelId;
    }

    public StringFilter getSystemModels() {
        return systemModels;
    }

    public void setSystemModels(StringFilter systemModels) {
        this.systemModels = systemModels;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public StringFilter getGlobal() {
        return global;
    }

    public void setGlobal(StringFilter global) {
        this.global = global;
    }

    public StringFilter getUom() {
        return uom;
    }

    public void setUom(StringFilter uom) {
        this.uom = uom;
    }

    public StringFilter getUomWeight() {
        return uomWeight;
    }

    public void setUomWeight(StringFilter uomWeight) {
        this.uomWeight = uomWeight;
    }

    public StringFilter getCategory() {
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
                Objects.equals(code, that.code) &&
                Objects.equals(description, that.description) &&
                Objects.equals(codeDescription, that.codeDescription) &&
                Objects.equals(weight, that.weight) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(uomId, that.uomId) &&
                Objects.equals(uomWeightId, that.uomWeightId) &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(systemModelId, that.systemModelId) &&
                Objects.equals(systemModels, that.systemModels) &&
                Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, codeDescription, weight, picture, uomId, uomWeightId, categoryId, systemModelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (codeDescription != null ? "codeDescription=" + codeDescription + ", " : "") +
            (weight != null ? "weight=" + weight + ", " : "") +
            (picture != null ? "picture=" + picture + ", " : "") +
            (uomId != null ? "uomId=" + uomId + ", " : "") +
            (uomWeightId != null ? "uomWeightId=" + uomWeightId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (systemModelId != null ? "systemModelId=" + systemModelId + ", " : "") +
            (systemModels != null ? "systemModel=" + systemModels + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            (global != null ? "global=" + global + ", " : "") +
            "}";
    }
}
