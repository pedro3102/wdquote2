package com.sapwd.wdquote.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.MovementDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovementDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal unitCost;

    @NotNull
    private BigDecimal qty;

    @NotNull
    private BigDecimal salePrice;

    @Size(max = 30)
    private String vendorCode;

    @NotNull
    private BigDecimal inventoryQty;

    private MovementDTO movement;

    private ProductDTO product;

    private InventoryDTO inventory;

    private StockPositionDTO stockPosition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public BigDecimal getInventoryQty() {
        return inventoryQty;
    }

    public void setInventoryQty(BigDecimal inventoryQty) {
        this.inventoryQty = inventoryQty;
    }

    public MovementDTO getMovement() {
        return movement;
    }

    public void setMovement(MovementDTO movement) {
        this.movement = movement;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public InventoryDTO getInventory() {
        return inventory;
    }

    public void setInventory(InventoryDTO inventory) {
        this.inventory = inventory;
    }

    public StockPositionDTO getStockPosition() {
        return stockPosition;
    }

    public void setStockPosition(StockPositionDTO stockPosition) {
        this.stockPosition = stockPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovementDetailsDTO)) {
            return false;
        }

        MovementDetailsDTO movementDetailsDTO = (MovementDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, movementDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovementDetailsDTO{" +
            "id=" + getId() +
            ", unitCost=" + getUnitCost() +
            ", qty=" + getQty() +
            ", salePrice=" + getSalePrice() +
            ", vendorCode='" + getVendorCode() + "'" +
            ", inventoryQty=" + getInventoryQty() +
            ", movement=" + getMovement() +
            ", product=" + getProduct() +
            ", inventory=" + getInventory() +
            ", stockPosition=" + getStockPosition() +
            "}";
    }
}
