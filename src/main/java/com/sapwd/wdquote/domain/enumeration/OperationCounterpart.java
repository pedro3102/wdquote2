package com.sapwd.wdquote.domain.enumeration;

/**
 * The OperationCounterpart enumeration.
 */
public enum OperationCounterpart {
    NONE(0, "NONE", "None"),
    VENDOR(1, "VENDOR", "Vendors and suppliers"),
    LOCATION(2, "LOCATION", "Internal areas"),
    CUSTOMER(3, "CUSTOMER", "Clients");

    final Integer id;
    final String name;
    final String description;

    OperationCounterpart(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static OperationCounterpart getById(Integer id) {
        if (null == id) {
            return null;
        }
        for (OperationCounterpart operationCounterpart : values()) {
            if (operationCounterpart.id.equals(id)) {
                return operationCounterpart;
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
