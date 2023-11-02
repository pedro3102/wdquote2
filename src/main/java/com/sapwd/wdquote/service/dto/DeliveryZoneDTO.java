package com.sapwd.wdquote.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.DeliveryZone} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryZoneDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String code;

    @NotNull
    @Size(max = 50)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryZoneDTO)) {
            return false;
        }

        DeliveryZoneDTO deliveryZoneDTO = (DeliveryZoneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deliveryZoneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryZoneDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
