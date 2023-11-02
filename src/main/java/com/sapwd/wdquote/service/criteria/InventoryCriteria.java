package com.sapwd.wdquote.service.criteria;

import com.sapwd.wdquote.domain.enumeration.StockStatus;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.sapwd.wdquote.domain.Inventory} entity. This class is used
 * in {@link com.sapwd.wdquote.web.rest.InventoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inventories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;
    private LongFilter id;
    private BigDecimalFilter qty;
    private BigDecimalFilter unitCost;
    private LocalDateFilter lastActivityDate;
    private StringFilter shelf;
    private BigDecimalFilter reorderPoint;
    private BigDecimalFilter reorderStatus;
    private StockStatusFilter stockStatus;
    private IntegerFilter vendorLeadTime;
    private LongFilter locationId;
    private LongFilter productId;
    private Boolean distinct;
    private StringFilter code;
    private StringFilter description;
    private StringFilter uom;
    private StringFilter product;
    private StringFilter global;

    public InventoryCriteria() {
    }

    public InventoryCriteria(InventoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.qty = other.qty == null ? null : other.qty.copy();
        this.unitCost = other.unitCost == null ? null : other.unitCost.copy();
        this.lastActivityDate = other.lastActivityDate == null ? null : other.lastActivityDate.copy();
        this.shelf = other.shelf == null ? null : other.shelf.copy();
        this.reorderPoint = other.reorderPoint == null ? null : other.reorderPoint.copy();
        this.reorderStatus = other.reorderStatus == null ? null : other.reorderStatus.copy();
        this.stockStatus = other.stockStatus == null ? null : other.stockStatus.copy();
        this.vendorLeadTime = other.vendorLeadTime == null ? null : other.vendorLeadTime.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.uom = other.uom == null ? null : other.uom.copy();
        this.product = other.product == null ? null : other.product.copy();
        this.global = other.global == null ? null : other.global.copy();
    }

    @Override
    public InventoryCriteria copy() {
        return new InventoryCriteria(this);
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

    public BigDecimalFilter getQty() {
        return qty;
    }

    public void setQty(BigDecimalFilter qty) {
        this.qty = qty;
    }

    public BigDecimalFilter qty() {
        if (qty == null) {
            qty = new BigDecimalFilter();
        }
        return qty;
    }

    public BigDecimalFilter getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimalFilter unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimalFilter unitCost() {
        if (unitCost == null) {
            unitCost = new BigDecimalFilter();
        }
        return unitCost;
    }

    public LocalDateFilter getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDateFilter lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public LocalDateFilter lastActivityDate() {
        if (lastActivityDate == null) {
            lastActivityDate = new LocalDateFilter();
        }
        return lastActivityDate;
    }

    public StringFilter getShelf() {
        return shelf;
    }

    public void setShelf(StringFilter shelf) {
        this.shelf = shelf;
    }

    public StringFilter shelf() {
        if (shelf == null) {
            shelf = new StringFilter();
        }
        return shelf;
    }

    public BigDecimalFilter getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(BigDecimalFilter reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public BigDecimalFilter reorderPoint() {
        if (reorderPoint == null) {
            reorderPoint = new BigDecimalFilter();
        }
        return reorderPoint;
    }

    public BigDecimalFilter getReorderStatus() {
        return reorderStatus;
    }

    public void setReorderStatus(BigDecimalFilter reorderStatus) {
        this.reorderStatus = reorderStatus;
    }

    public StockStatusFilter getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatusFilter stockStatus) {
        this.stockStatus = stockStatus;
    }

    public IntegerFilter getVendorLeadTime() {
        return vendorLeadTime;
    }

    public void setVendorLeadTime(IntegerFilter vendorLeadTime) {
        this.vendorLeadTime = vendorLeadTime;
    }

    public IntegerFilter vendorLeadTime() {
        if (vendorLeadTime == null) {
            vendorLeadTime = new IntegerFilter();
        }
        return vendorLeadTime;
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

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getUom() {
        return uom;
    }

    public void setUom(StringFilter uom) {
        this.uom = uom;
    }

    public StringFilter getProduct() {
        return product;
    }

    public void setProduct(StringFilter product) {
        this.product = product;
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
        final InventoryCriteria that = (InventoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
                Objects.equals(qty, that.qty) &&
                Objects.equals(unitCost, that.unitCost) &&
                Objects.equals(lastActivityDate, that.lastActivityDate) &&
                Objects.equals(shelf, that.shelf) &&
                Objects.equals(reorderPoint, that.reorderPoint) &&
                Objects.equals(vendorLeadTime, that.vendorLeadTime) &&
                Objects.equals(locationId, that.locationId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(distinct, that.distinct) &&
                Objects.equals(code, that.code) &&
                Objects.equals(description, that.description) &&
                Objects.equals(uom, that.uom) &&
                Objects.equals(product, that.product) &&
                Objects.equals(global, that.global)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            qty,
            unitCost,
            lastActivityDate,
            shelf,
            reorderPoint,
            vendorLeadTime,
            locationId,
            productId,
            distinct,
            code,
            description,
            uom,
            product,
            global
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (qty != null ? "qty=" + qty + ", " : "") +
            (unitCost != null ? "unitCost=" + unitCost + ", " : "") +
            (lastActivityDate != null ? "lastActivityDate=" + lastActivityDate + ", " : "") +
            (shelf != null ? "shelf=" + shelf + ", " : "") +
            (reorderPoint != null ? "reorderPoint=" + reorderPoint + ", " : "") +
            (vendorLeadTime != null ? "vendorLeadTime=" + vendorLeadTime + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (uom != null ? "uom=" + uom + ", " : "") +
            (product != null ? "product=" + product + ", " : "") +
            (global != null ? "uom=" + global + ", " : "") +
            "}";
    }

    /**
     * Class for filtering MovementStatus
     */
    public static class StockStatusFilter extends Filter<StockStatus> {

        public StockStatusFilter() {
        }

        public StockStatusFilter(InventoryCriteria.StockStatusFilter filter) {
            super(filter);
        }

        @Override
        public InventoryCriteria.StockStatusFilter copy() {
            return new InventoryCriteria.StockStatusFilter(this);
        }
    }
}
