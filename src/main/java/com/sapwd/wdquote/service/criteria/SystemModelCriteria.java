package com.sapwd.wdquote.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.sapwd.wdquote.domain.SystemModel} entity. This class is used
 * in {@link com.sapwd.wdquote.web.rest.SystemModelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /system-models?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemModelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter picture;

    private StringFilter system;

    private LongFilter systemId;

    private LongFilter productId;

    private Boolean distinct;

    private StringFilter global;

    public SystemModelCriteria() {}

    public SystemModelCriteria(SystemModelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.picture = other.picture == null ? null : other.picture.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.system = other.system == null ? null : other.system.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
        this.global = other.global;
    }

    @Override
    public SystemModelCriteria copy() {
        return new SystemModelCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getPicture() {
        return picture;
    }

    public StringFilter picture() {
        if (picture == null) {
            picture = new StringFilter();
        }
        return picture;
    }

    public void setPicture(StringFilter picture) {
        this.picture = picture;
    }

    public StringFilter getSystem() {
        return system;
    }

    public void setSystem(StringFilter system) {
        this.system = system;
    }

    public LongFilter getSystemId() {
        return systemId;
    }

    public LongFilter systemId() {
        if (systemId == null) {
            systemId = new LongFilter();
        }
        return systemId;
    }

    public void setSystemId(LongFilter systemId) {
        this.systemId = systemId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SystemModelCriteria that = (SystemModelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(picture, that.picture) &&
            Objects.equals(systemId, that.systemId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct) &&
            Objects.equals(global, that.global)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, picture, systemId, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemModelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (picture != null ? "picture=" + picture + ", " : "") +
            (systemId != null ? "systemId=" + systemId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
