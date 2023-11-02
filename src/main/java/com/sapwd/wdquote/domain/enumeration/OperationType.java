package com.sapwd.wdquote.domain.enumeration;

/**
 * The OperationType enumeration.
 */
public enum OperationType {
    IN(1, "IN", "Input Operation"),
    OUT(2, "OUT", "Output Operation");

    OperationType(Integer id, String name, String description) {
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

    public static OperationType getById(Integer id) {
        if (null == id) {
            return null;
        }
        for (OperationType operationType : values()) {
            if (operationType.id.equals(id)) {
                return operationType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
