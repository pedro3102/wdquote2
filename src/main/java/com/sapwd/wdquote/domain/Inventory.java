package com.sapwd.wdquote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sapwd.wdquote.domain.enumeration.StockStatus;
import com.sapwd.wdquote.domain.enumeration.converter.StockStatusConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

/**
 * A Inventory.
 */
@Entity
@Table(name = "inventory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inventory extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "qty", precision = 21, scale = 2, nullable = false)
    private BigDecimal qty;

    @NotNull
    @Column(name = "unit_cost", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitCost;

    @NotNull
    @Column(name = "last_activity_date", nullable = false)
    private LocalDate lastActivityDate;

    @Size(max = 50)
    @Column(name = "shelf", length = 50)
    private String shelf;

    @Column(name = "reorder_point", precision = 21, scale = 2)
    private BigDecimal reorderPoint = BigDecimal.ZERO;

    @Formula(value = "qty - reorder_point")
    private BigDecimal reorderStatus;

    @Formula(value = "(CASE WHEN qty <= 0 THEN 3 WHEN qty < reorder_point THEN 2 WHEN qty >= reorder_point THEN 1 END)")
    @Convert(converter = StockStatusConverter.class)
    private StockStatus stockStatus;

    @Column(name = "vendor_lead_time")
    private Integer vendorLeadTime;

    @ManyToOne(optional = false)
    @NotNull
    private Location location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "uom", "uomWeight", "category", "systemModels" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inventory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public Inventory qty(BigDecimal qty) {
        this.setQty(qty);
        return this;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitCost() {
        return this.unitCost;
    }

    public Inventory unitCost(BigDecimal unitCost) {
        this.setUnitCost(unitCost);
        return this;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public LocalDate getLastActivityDate() {
        return this.lastActivityDate;
    }

    public Inventory lastActivityDate(LocalDate lastActivityDate) {
        this.setLastActivityDate(lastActivityDate);
        return this;
    }

    public void setLastActivityDate(LocalDate lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public String getShelf() {
        return this.shelf;
    }

    public Inventory shelf(String shelf) {
        this.setShelf(shelf);
        return this;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public BigDecimal getReorderPoint() {
        return this.reorderPoint;
    }

    public Inventory reorderPoint(BigDecimal reorderPoint) {
        this.setReorderPoint(reorderPoint);
        return this;
    }

    public BigDecimal getReorderStatus() {
        return reorderStatus;
    }

    public void setReorderStatus(BigDecimal reorderStatus) {
        this.reorderStatus = reorderStatus;
    }

    public void setReorderPoint(BigDecimal reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public Integer getVendorLeadTime() {
        return this.vendorLeadTime;
    }

    public Inventory vendorLeadTime(Integer vendorLeadTime) {
        this.setVendorLeadTime(vendorLeadTime);
        return this;
    }

    public void setVendorLeadTime(Integer vendorLeadTime) {
        this.vendorLeadTime = vendorLeadTime;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Inventory location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Inventory product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventory)) {
            return false;
        }
        return id != null && id.equals(((Inventory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            ", qty=" + getQty() +
            ", unitCost=" + getUnitCost() +
            ", lastActivityDate='" + getLastActivityDate() + "'" +
            ", shelf='" + getShelf() + "'" +
            ", reorderPoint=" + getReorderPoint() +
            ", vendorLeadTime=" + getVendorLeadTime() +
            "}";
    }
}
