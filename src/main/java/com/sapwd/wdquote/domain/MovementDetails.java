package com.sapwd.wdquote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MovementDetails.
 */
@Entity
@Table(name = "movement_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovementDetails extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "unit_cost", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitCost;

    @NotNull
    @Column(name = "qty", precision = 21, scale = 2, nullable = false)
    private BigDecimal qty;

    @Column(name = "sale_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal salePrice;

    @Size(max = 30)
    @Column(name = "vendor_code", length = 30)
    private String vendorCode;

    @NotNull
    @Column(name = "inventory_qty", precision = 21, scale = 2, nullable = false)
    private BigDecimal inventoryQty;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "movementType", "location", "counterpartLocation", "counterpartVendor", "counterpartCustomer" },
        allowSetters = true
    )
    private Movement movement;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "uom", "uomWeight", "category", "systemModels" }, allowSetters = true)
    private Product product;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties(value = { "location", "product" }, allowSetters = true)
    private Inventory inventory;

    @ManyToOne
    private StockPosition stockPosition;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovementDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public BigDecimal getUnitCost() {
        return this.unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public MovementDetails unitCost(BigDecimal unitCost) {
        this.setUnitCost(unitCost);
        return this;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public MovementDetails qty(BigDecimal qty) {
        this.setQty(qty);
        return this;
    }

    public BigDecimal getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public MovementDetails salePrice(BigDecimal salePrice) {
        this.setSalePrice(salePrice);
        return this;
    }

    public String getVendorCode() {
        return this.vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public MovementDetails vendorCode(String vendorCode) {
        this.setVendorCode(vendorCode);
        return this;
    }

    public BigDecimal getInventoryQty() {
        return this.inventoryQty;
    }

    public void setInventoryQty(BigDecimal inventoryQty) {
        this.inventoryQty = inventoryQty;
    }

    public MovementDetails inventoryQty(BigDecimal inventoryQty) {
        this.setInventoryQty(inventoryQty);
        return this;
    }

    public Movement getMovement() {
        return this.movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public MovementDetails movement(Movement movement) {
        this.setMovement(movement);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public MovementDetails product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public MovementDetails inventory(Inventory inventory) {
        this.setInventory(inventory);
        return this;
    }

    public StockPosition getStockPosition() {
        return this.stockPosition;
    }

    public void setStockPosition(StockPosition stockPosition) {
        this.stockPosition = stockPosition;
    }

    public MovementDetails stockPosition(StockPosition stockPosition) {
        this.setStockPosition(stockPosition);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovementDetails)) {
            return false;
        }
        return id != null && id.equals(((MovementDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovementDetails{" +
            "id=" + getId() +
            ", unitCost=" + getUnitCost() +
            ", qty=" + getQty() +
            ", salePrice=" + getSalePrice() +
            ", vendorCode='" + getVendorCode() + "'" +
            ", inventoryQty=" + getInventoryQty() +
            "}";
    }
}
