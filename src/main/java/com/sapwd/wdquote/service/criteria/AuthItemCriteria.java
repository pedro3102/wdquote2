package com.sapwd.wdquote.service.criteria;

import java.io.Serializable;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.sapwd.wdquote.domain.AuthItem} entity. This class is used
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuthItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;
    private StringFilter name;
    private StringFilter description;
    private IntegerFilter type;
    private LongFilter globalId;
    private StringFilter globalFilter;

    public AuthItemCriteria() {}

    public AuthItemCriteria(AuthItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.globalId = other.globalId == null ? null : other.globalId.copy();
        this.globalFilter = other.globalFilter == null ? null : other.globalFilter.copy();
    }

    @Override
    public AuthItemCriteria copy() {
        return new AuthItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getType() {
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
    }

    public LongFilter getGlobalId() {
        return globalId;
    }

    public void setGlobalId(LongFilter globalId) {
        this.globalId = globalId;
    }

    public StringFilter getGlobalFilter() {
        return globalFilter;
    }

    public void setGlobalFilter(StringFilter globalFilter) {
        this.globalFilter = globalFilter;
    }
}
