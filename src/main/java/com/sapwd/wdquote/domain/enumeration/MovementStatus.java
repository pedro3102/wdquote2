package com.sapwd.wdquote.domain.enumeration;

/**
 * The MovementStatus enumeration.
 */
public enum MovementStatus {
    PENDING(1, "PENDING", "Pending"),
    COMPLETED(2, "COMPLETED", "Completed"),
    CANCELED(3, "CANCELED", "Canceled");

    final Integer id;
    final String name;
    final String description;
    MovementStatus(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static MovementStatus getById(Integer id) {
        if (null == id) {
            return null;
        }
        for (MovementStatus movementStatus : values()) {
            if (movementStatus.id.equals(id)) {
                return movementStatus;
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
