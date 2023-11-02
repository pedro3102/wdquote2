package com.sapwd.wdquote.service.criteria;

import com.sapwd.wdquote.domain.enumeration.MovementStatus;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.sapwd.wdquote.domain.Movement} entity. This class is used
 * in {@link com.sapwd.wdquote.web.rest.MovementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /movements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;
    private LongFilter id;
    private StringFilter no;
    private StringFilter reference;
    private LocalDateFilter date;
    private StringFilter note;
    private LocalDateFilter canceledDate;
    private MovementStatusFilter status;
    private LongFilter movementTypeId;
    private LongFilter locationId;
    private LongFilter counterpartLocationId;
    private LongFilter counterpartVendorId;
    private LongFilter counterpartCustomerId;
    private StringFilter counterpart;
    private Boolean distinct;
    private StringFilter movementType;
    private StringFilter location;
    private StringFilter global;

    public MovementCriteria() {
    }

    public MovementCriteria(MovementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.no = other.no == null ? null : other.no.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.canceledDate = other.canceledDate == null ? null : other.canceledDate.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.movementTypeId = other.movementTypeId == null ? null : other.movementTypeId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.counterpartLocationId = other.counterpartLocationId == null ? null : other.counterpartLocationId.copy();
        this.counterpartVendorId = other.counterpartVendorId == null ? null : other.counterpartVendorId.copy();
        this.counterpartCustomerId = other.counterpartCustomerId == null ? null : other.counterpartCustomerId.copy();
        this.counterpartCustomerId = other.counterpartCustomerId == null ? null : other.counterpartCustomerId.copy();
        this.counterpart = other.counterpart;
        this.movementType = other.movementType == null ? null : other.movementType.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.global = other.global == null ? null : other.global.copy();
    }

    @Override
    public MovementCriteria copy() {
        return new MovementCriteria(this);
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

    public StringFilter getNo() {
        return no;
    }

    public void setNo(StringFilter no) {
        this.no = no;
    }

    public StringFilter no() {
        if (no == null) {
            no = new StringFilter();
        }
        return no;
    }

    public StringFilter getReference() {
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public StringFilter reference() {
        if (reference == null) {
            reference = new StringFilter();
        }
        return reference;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public StringFilter note() {
        if (note == null) {
            note = new StringFilter();
        }
        return note;
    }

    public LocalDateFilter getCanceledDate() {
        return canceledDate;
    }

    public void setCanceledDate(LocalDateFilter canceledDate) {
        this.canceledDate = canceledDate;
    }

    public LocalDateFilter canceledDate() {
        if (canceledDate == null) {
            canceledDate = new LocalDateFilter();
        }
        return canceledDate;
    }

    public MovementStatusFilter getStatus() {
        return status;
    }

    public void setStatus(MovementStatusFilter status) {
        this.status = status;
    }

    public MovementStatusFilter status() {
        if (status == null) {
            status = new MovementStatusFilter();
        }
        return status;
    }

    public LongFilter getMovementTypeId() {
        return movementTypeId;
    }

    public void setMovementTypeId(LongFilter movementTypeId) {
        this.movementTypeId = movementTypeId;
    }

    public LongFilter movementTypeId() {
        if (movementTypeId == null) {
            movementTypeId = new LongFilter();
        }
        return movementTypeId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter locationId() {
        if (locationId == null) {
            locationId = new LongFilter();
        }
        return locationId;
    }

    public LongFilter getCounterpartLocationId() {
        return counterpartLocationId;
    }

    public void setCounterpartLocationId(LongFilter counterpartLocationId) {
        this.counterpartLocationId = counterpartLocationId;
    }

    public LongFilter counterpartLocationId() {
        if (counterpartLocationId == null) {
            counterpartLocationId = new LongFilter();
        }
        return counterpartLocationId;
    }

    public LongFilter getCounterpartVendorId() {
        return counterpartVendorId;
    }

    public void setCounterpartVendorId(LongFilter counterpartVendorId) {
        this.counterpartVendorId = counterpartVendorId;
    }

    public LongFilter counterpartVendorId() {
        if (counterpartVendorId == null) {
            counterpartVendorId = new LongFilter();
        }
        return counterpartVendorId;
    }

    public LongFilter getCounterpartCustomerId() {
        return counterpartCustomerId;
    }

    public void setCounterpartCustomerId(LongFilter counterpartCustomerId) {
        this.counterpartCustomerId = counterpartCustomerId;
    }

    public LongFilter counterpartCustomerId() {
        if (counterpartCustomerId == null) {
            counterpartCustomerId = new LongFilter();
        }
        return counterpartCustomerId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public StringFilter getMovementType() {
        return movementType;
    }

    public void setMovementType(StringFilter movementType) {
        this.movementType = movementType;
    }

    public StringFilter movementType() {
        if (movementType == null) {
            movementType = new StringFilter();
        }
        return movementType;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public StringFilter getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(StringFilter counterpart) {
        this.counterpart = counterpart;
    }

    public StringFilter counterpart() {
        if (counterpart == null) {
            counterpart = new StringFilter();
        }
        return counterpart;
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
        final MovementCriteria that = (MovementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
                Objects.equals(no, that.no) &&
                Objects.equals(reference, that.reference) &&
                Objects.equals(date, that.date) &&
                Objects.equals(note, that.note) &&
                Objects.equals(canceledDate, that.canceledDate) &&
                Objects.equals(status, that.status) &&
                Objects.equals(movementTypeId, that.movementTypeId) &&
                Objects.equals(locationId, that.locationId) &&
                Objects.equals(counterpartLocationId, that.counterpartLocationId) &&
                Objects.equals(counterpartVendorId, that.counterpartVendorId) &&
                Objects.equals(counterpartCustomerId, that.counterpartCustomerId) &&
                Objects.equals(counterpart, that.counterpart) &&
                Objects.equals(distinct, that.distinct) &&
                Objects.equals(movementType, that.movementType) &&
                Objects.equals(location, that.location)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            no,
            reference,
            date,
            note,
            canceledDate,
            status,
            movementTypeId,
            locationId,
            counterpartLocationId,
            counterpartVendorId,
            counterpartCustomerId,
            counterpart,
            distinct,
            movementType,
            location
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (no != null ? "no=" + no + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (canceledDate != null ? "canceledDate=" + canceledDate + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (movementTypeId != null ? "movementTypeId=" + movementTypeId + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (counterpartLocationId != null ? "counterpartLocationId=" + counterpartLocationId + ", " : "") +
            (counterpartVendorId != null ? "counterpartVendorId=" + counterpartVendorId + ", " : "") +
            (counterpartCustomerId != null ? "counterpartCustomerId=" + counterpartCustomerId + ", " : "") +
            (counterpart != null ? "counterpart=" + counterpart + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            (movementType != null ? "movementType=" + movementType + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            "}";
    }

    /**
     * Class for filtering MovementStatus
     */
    public static class MovementStatusFilter extends Filter<MovementStatus> {

        public MovementStatusFilter() {
        }

        public MovementStatusFilter(MovementStatusFilter filter) {
            super(filter);
        }

        @Override
        public MovementStatusFilter copy() {
            return new MovementStatusFilter(this);
        }
    }
}
