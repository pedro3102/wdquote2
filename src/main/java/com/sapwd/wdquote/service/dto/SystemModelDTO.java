package com.sapwd.wdquote.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.SystemModel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemModelDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 255)
    private String description;

    private String picture;

    private SystemaDTO system;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public SystemaDTO getSystem() {
        return system;
    }

    public void setSystem(SystemaDTO system) {
        this.system = system;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemModelDTO)) {
            return false;
        }

        SystemModelDTO systemModelDTO = (SystemModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemModelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemModelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", picture='" + getPicture() + "'" +
            ", system=" + getSystem() +
            "}";
    }
}
