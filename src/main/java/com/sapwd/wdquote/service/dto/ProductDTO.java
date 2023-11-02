package com.sapwd.wdquote.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String code;

    @NotNull
    @Size(max = 255)
    private String description;

    private BigDecimal weight;

    private String picture;

    private UnitOfMeasureDTO uom;

    private UnitOfMeasureDTO uomWeight;

    private ProductCategoryDTO category;

    private Set<SystemModelDTO> systemModels = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public UnitOfMeasureDTO getUom() {
        return uom;
    }

    public void setUom(UnitOfMeasureDTO uom) {
        this.uom = uom;
    }

    public UnitOfMeasureDTO getUomWeight() {
        return uomWeight;
    }

    public void setUomWeight(UnitOfMeasureDTO uomWeight) {
        this.uomWeight = uomWeight;
    }

    public ProductCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ProductCategoryDTO category) {
        this.category = category;
    }

    public Set<SystemModelDTO> getSystemModels() {
        return systemModels;
    }

    public void setSystemModels(Set<SystemModelDTO> systemModels) {
        this.systemModels = systemModels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", picture='" + getPicture() + "'" +
            ", uom=" + getUom() +
            ", uomWeight=" + getUomWeight() +
            ", category=" + getCategory() +
            ", systemModels=" + getSystemModels() +
            "}";
    }
}
