package com.sapwd.wdquote.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.sapwd.wdquote.domain.MovementDetails} entity. This class is used
 * in {@link com.sapwd.wdquote.web.rest.MovementDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /movement-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovementDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter unitCost;

    private BigDecimalFilter qty;

    private BigDecimalFilter salePrice;

    private StringFilter vendorCode;

    private BigDecimalFilter inventoryQty;

    private LongFilter movementId;

    private LongFilter productId;

    private LongFilter inventoryId;

    private LongFilter stockPositionId;

    private Boolean distinct;

    public MovementDetailsCriteria() {}

    public MovementDetailsCriteria(MovementDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.unitCost = other.unitCost == null ? null : other.unitCost.copy();
        this.qty = other.qty == null ? null : other.qty.copy();
        this.salePrice = other.salePrice == null ? null : other.salePrice.copy();
        this.vendorCode = other.vendorCode == null ? null : other.vendorCode.copy();
        this.inventoryQty = other.inventoryQty == null ? null : other.inventoryQty.copy();
        this.movementId = other.movementId == null ? null : other.movementId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.inventoryId = other.inventoryId == null ? null : other.inventoryId.copy();
        this.stockPositionId = other.stockPositionId == null ? null : other.stockPositionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MovementDetailsCriteria copy() {
        return new MovementDetailsCriteria(this);
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

    public BigDecimalFilter getUnitCost() {
        return unitCost;
    }

    public BigDecimalFilter unitCost() {
        if (unitCost == null) {
            unitCost = new BigDecimalFilter();
        }
        return unitCost;
    }

    public void setUnitCost(BigDecimalFilter unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimalFilter getQty() {
        return qty;
    }

    public BigDecimalFilter qty() {
        if (qty == null) {
            qty = new BigDecimalFilter();
        }
        return qty;
    }

    public void setQty(BigDecimalFilter qty) {
        this.qty = qty;
    }

    public BigDecimalFilter getSalePrice() {
        return salePrice;
    }

    public BigDecimalFilter salePrice() {
        if (salePrice == null) {
            salePrice = new BigDecimalFilter();
        }
        return salePrice;
    }

    public void setSalePrice(BigDecimalFilter salePrice) {
        this.salePrice = salePrice;
    }

    public StringFilter getVendorCode() {
        return vendorCode;
    }

    public StringFilter vendorCode() {
        if (vendorCode == null) {
            vendorCode = new StringFilter();
        }
        return vendorCode;
    }

    public void setVendorCode(StringFilter vendorCode) {
        this.vendorCode = vendorCode;
    }

    public BigDecimalFilter getInventoryQty() {
        return inventoryQty;
    }

    public BigDecimalFilter inventoryQty() {
        if (inventoryQty == null) {
            inventoryQty = new BigDecimalFilter();
        }
        return inventoryQty;
    }

    public void setInventoryQty(BigDecimalFilter inventoryQty) {
        this.inventoryQty = inventoryQty;
    }

    public LongFilter getMovementId() {
        return movementId;
    }

    public LongFilter movementId() {
        if (movementId == null) {
            movementId = new LongFilter();
        }
        return movementId;
    }

    public void setMovementId(LongFilter movementId) {
        this.movementId = movementId;
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

    public LongFilter getInventoryId() {
        return inventoryId;
    }

    public LongFilter inventoryId() {
        if (inventoryId == null) {
            inventoryId = new LongFilter();
        }
        return inventoryId;
    }

    public void setInventoryId(LongFilter inventoryId) {
        this.inventoryId = inventoryId;
    }

    public LongFilter getStockPositionId() {
        return stockPositionId;
    }

    public LongFilter stockPositionId() {
        if (stockPositionId == null) {
            stockPositionId = new LongFilter();
        }
        return stockPositionId;
    }

    public void setStockPositionId(LongFilter stockPositionId) {
        this.stockPositionId = stockPositionId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MovementDetailsCriteria that = (MovementDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(unitCost, that.unitCost) &&
            Objects.equals(qty, that.qty) &&
            Objects.equals(salePrice, that.salePrice) &&
            Objects.equals(vendorCode, that.vendorCode) &&
            Objects.equals(inventoryQty, that.inventoryQty) &&
            Objects.equals(movementId, that.movementId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(inventoryId, that.inventoryId) &&
            Objects.equals(stockPositionId, that.stockPositionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            unitCost,
            qty,
            salePrice,
            vendorCode,
            inventoryQty,
            movementId,
            productId,
            inventoryId,
            stockPositionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovementDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (unitCost != null ? "unitCost=" + unitCost + ", " : "") +
            (qty != null ? "qty=" + qty + ", " : "") +
            (salePrice != null ? "salePrice=" + salePrice + ", " : "") +
            (vendorCode != null ? "vendorCode=" + vendorCode + ", " : "") +
            (inventoryQty != null ? "inventoryQty=" + inventoryQty + ", " : "") +
            (movementId != null ? "movementId=" + movementId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (inventoryId != null ? "inventoryId=" + inventoryId + ", " : "") +
            (stockPositionId != null ? "stockPositionId=" + stockPositionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
