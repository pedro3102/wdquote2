package com.sapwd.wdquote.service.dto;

import com.sapwd.wdquote.domain.enumeration.LocationType;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.Location} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 5)
    private String code;

    @NotNull
    @Size(max = 25)
    private String name;

    private String codeName;

    @Size(max = 255)
    private String address;

    @NotNull
    private Boolean isWarehouse;

    @NotNull
    private LocationType locationType;

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

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsWarehouse() {
        return isWarehouse;
    }

    public void setIsWarehouse(Boolean isWarehouse) {
        this.isWarehouse = isWarehouse;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDTO)) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", isWarehouse='" + getIsWarehouse() + "'" +
            ", locationType='" + getLocationType() + "'" +
            "}";
    }
}
