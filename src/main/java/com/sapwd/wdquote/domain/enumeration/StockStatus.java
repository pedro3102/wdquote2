package com.sapwd.wdquote.domain.enumeration;

/**
 * The StockStatus enumeration.
 */
public enum StockStatus {
    IN_STOCK(1, "IN_STOCK", "In Stock"),
    LOW_STOCK(2, "LOW_STOCK", "Low Stock"),
    OUT_STOCK(3, "OUT_STOCK", "Out Stock");

    StockStatus(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    final Integer id;
    final String name;
    final String description;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static StockStatus getById(Integer id) {
        if (null == id) {
            return null;
        }
        for (StockStatus stockStatus : values()) {
            if (stockStatus.id.equals(id)) {
                return stockStatus;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
