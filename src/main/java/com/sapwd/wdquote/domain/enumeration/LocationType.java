package com.sapwd.wdquote.domain.enumeration;

/**
 * The LocationType enumeration.
 */
public enum LocationType {
    WAREHOUSE(1, "WAREHOUSE", "Warehouse"),
    WIP(2, "WIP", "Work in Progress"),
    ON_PAINT(3, "ON_PAINT", "Pending");

    final Integer id;
    final String name;
    final String description;

    LocationType(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static LocationType getById(Integer id) {
        if (null == id) {
            return null;
        }
        for (LocationType locationType : values()) {
            if (locationType.id.equals(id)) {
                return locationType;
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
