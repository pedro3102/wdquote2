package com.sapwd.wdquote.service.dto;

import com.sapwd.wdquote.domain.enumeration.StockStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.Inventory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal qty;

    @NotNull
    private BigDecimal unitCost;

    @NotNull
    private LocalDate lastActivityDate;

    @Size(max = 50)
    private String shelf;

    private BigDecimal reorderPoint;

    private BigDecimal reorderStatus;

    private StockStatus stockStatus;

    private Integer vendorLeadTime;

    private LocationDTO location;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public LocalDate getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDate lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public BigDecimal getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(BigDecimal reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public BigDecimal getReorderStatus() {
        return reorderStatus;
    }

    public void setReorderStatus(BigDecimal reorderStatus) {
        this.reorderStatus = reorderStatus;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public Integer getVendorLeadTime() {
        return vendorLeadTime;
    }

    public void setVendorLeadTime(Integer vendorLeadTime) {
        this.vendorLeadTime = vendorLeadTime;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryDTO)) {
            return false;
        }

        InventoryDTO inventoryDTO = (InventoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryDTO{" +
            "id=" + getId() +
            ", qty=" + getQty() +
            ", unitCost=" + getUnitCost() +
            ", lastActivityDate='" + getLastActivityDate() + "'" +
            ", shelf='" + getShelf() + "'" +
            ", reorderPoint=" + getReorderPoint() +
            ", reorderStatus=" + getReorderStatus() +
            ", stockStatus=" + getStockStatus() +
            ", vendorLeadTime=" + getVendorLeadTime() +
            ", location=" + getLocation() +
            ", product=" + getProduct() +
            "}";
    }
}
