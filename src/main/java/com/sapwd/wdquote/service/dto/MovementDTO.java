package com.sapwd.wdquote.service.dto;

import com.sapwd.wdquote.domain.enumeration.MovementStatus;
import com.sapwd.wdquote.domain.enumeration.OperationCounterpart;
import com.sapwd.wdquote.domain.enumeration.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.Movement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovementDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 12)
    private String no;

    @Size(max = 20)
    private String reference;

    @NotNull
    private LocalDate date;

    @Size(max = 255)
    private String note;

    private LocalDate canceledDate;

    private Long consecutive;

    private MovementStatus status;

    private OperationType type;

    private OperationCounterpart counterpart;

    private MovementTypeDTO movementType;

    private LocationDTO location;

    private LocationDTO counterpartLocation;

    private VendorDTO counterpartVendor;

    private CustomerDTO counterpartCustomer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getCanceledDate() {
        return canceledDate;
    }

    public void setCanceledDate(LocalDate canceledDate) {
        this.canceledDate = canceledDate;
    }

    public Long getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(Long consecutive) {
        this.consecutive = consecutive;
    }

    public MovementStatus getStatus() {
        return status;
    }

    public void setStatus(MovementStatus status) {
        this.status = status;
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

    public MovementTypeDTO getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementTypeDTO movementType) {
        this.movementType = movementType;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public LocationDTO getCounterpartLocation() {
        return counterpartLocation;
    }

    public void setCounterpartLocation(LocationDTO counterpartLocation) {
        this.counterpartLocation = counterpartLocation;
    }

    public VendorDTO getCounterpartVendor() {
        return counterpartVendor;
    }

    public void setCounterpartVendor(VendorDTO counterpartVendor) {
        this.counterpartVendor = counterpartVendor;
    }

    public CustomerDTO getCounterpartCustomer() {
        return counterpartCustomer;
    }

    public void setCounterpartCustomer(CustomerDTO counterpartCustomer) {
        this.counterpartCustomer = counterpartCustomer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovementDTO)) {
            return false;
        }

        MovementDTO movementDTO = (MovementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, movementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovementDTO{" +
            "id=" + getId() +
            ", no='" + getNo() + "'" +
            ", reference='" + getReference() + "'" +
            ", date='" + getDate() + "'" +
            ", note='" + getNote() + "'" +
            ", canceledDate='" + getCanceledDate() + "'" +
            ", consecutive='" + getConsecutive() + "'" +
            ", status='" + getStatus() + "'" +
            ", movementType=" + getMovementType() +
            ", location=" + getLocation() +
            ", counterpartLocation=" + getCounterpartLocation() +
            ", counterpartVendor=" + getCounterpartVendor() +
            ", counterpartCustomer=" + getCounterpartCustomer() +
            "}";
    }
}
