package com.sapwd.wdquote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sapwd.wdquote.domain.enumeration.MovementStatus;
import com.sapwd.wdquote.domain.enumeration.OperationCounterpart;
import com.sapwd.wdquote.domain.enumeration.OperationType;
import com.sapwd.wdquote.domain.enumeration.converter.MovementStatusConverter;
import com.sapwd.wdquote.domain.enumeration.converter.OperationCounterpartConverter;
import com.sapwd.wdquote.domain.enumeration.converter.OperationTypeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Movement.
 */
@Entity
@Table(name = "movement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Movement extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 12)
    @Column(name = "no", length = 8, nullable = false)
    private String no;

    @Size(max = 20)
    @Column(name = "reference", length = 20)
    private String reference;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Size(max = 255)
    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "canceled_date")
    private LocalDate canceledDate;

    @Column(name = "completed")
    private Boolean completed = false;

    @Convert(converter = MovementStatusConverter.class)
    @Column(name = "status")
    private MovementStatus status;

    @NotNull
    @Convert(converter = OperationTypeConverter.class)
    @Column(name = "type", nullable = false)
    private OperationType type;

    @Column(name = "consecutive")
    private Long consecutive = 0L;

    @Convert(converter = OperationCounterpartConverter.class)
    @Column(name = "counterpart", nullable = false)
    private OperationCounterpart counterpart;

    @ManyToOne(optional = false)
    @NotNull
    private MovementType movementType;

    @ManyToOne(optional = false)
    @NotNull
    private Location location;

    @ManyToOne
    private Location counterpartLocation;

    @ManyToOne
    private Vendor counterpartVendor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "deliveryZone", "taxAreaCode" }, allowSetters = true)
    private Customer counterpartCustomer;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "movement", orphanRemoval = true)
    private Collection<MovementDetails> movementDetailsCollection = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movement id(Long id) {
        this.setId(id);
        return this;
    }

    public String getNo() {
        return this.no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Movement no(String no) {
        this.setNo(no);
        return this;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Movement reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Movement date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Movement note(String note) {
        this.setNote(note);
        return this;
    }

    public LocalDate getCanceledDate() {
        return this.canceledDate;
    }

    public void setCanceledDate(LocalDate canceledDate) {
        this.canceledDate = canceledDate;
    }

    public Movement canceledDate(LocalDate canceledDate) {
        this.setCanceledDate(canceledDate);
        return this;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public MovementStatus getStatus() {
        return this.status;
    }

    public void setStatus(MovementStatus status) {
        this.status = status;
    }

    public Movement status(MovementStatus status) {
        this.setStatus(status);
        return this;
    }

    public OperationType getType() {
        return this.type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public Movement type(OperationType type) {
        this.setType(type);
        return this;
    }

    public Long getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(Long consecutive) {
        this.consecutive = consecutive;
    }

    public OperationCounterpart getCounterpart() {
        return this.counterpart;
    }

    public void setCounterpart(OperationCounterpart counterpart) {
        this.counterpart = counterpart;
    }

    public Movement counterpart(OperationCounterpart counterpart) {
        this.setCounterpart(counterpart);
        return this;
    }

    public MovementType getMovementType() {
        return this.movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Movement movementType(MovementType movementType) {
        this.setMovementType(movementType);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Movement location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Location getCounterpartLocation() {
        return this.counterpartLocation;
    }

    public void setCounterpartLocation(Location location) {
        this.counterpartLocation = location;
    }

    public Movement counterpartLocation(Location location) {
        this.setCounterpartLocation(location);
        return this;
    }

    public Vendor getCounterpartVendor() {
        return this.counterpartVendor;
    }

    public void setCounterpartVendor(Vendor vendor) {
        this.counterpartVendor = vendor;
    }

    public Movement counterpartVendor(Vendor vendor) {
        this.setCounterpartVendor(vendor);
        return this;
    }

    public Customer getCounterpartCustomer() {
        return this.counterpartCustomer;
    }

    public void setCounterpartCustomer(Customer customer) {
        this.counterpartCustomer = customer;
    }

    public Movement counterpartCustomer(Customer customer) {
        this.setCounterpartCustomer(customer);
        return this;
    }

    public Collection<MovementDetails> getMovementDetailsCollection() {
        return movementDetailsCollection;
    }

    public void setMovementDetailsCollection(Collection<MovementDetails> movementDetailsCollection) {
        this.movementDetailsCollection = movementDetailsCollection;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movement)) {
            return false;
        }
        return id != null && id.equals(((Movement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Movement{" +
            "id=" + getId() +
            ", no='" + getNo() + "'" +
            ", reference='" + getReference() + "'" +
            ", date='" + getDate() + "'" +
            ", note='" + getNote() + "'" +
            ", canceledDate='" + getCanceledDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
