package com.sapwd.wdquote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "code", length = 30, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 255)
    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "weight", precision = 21, scale = 2)
    private BigDecimal weight;

    @Column(name = "picture")
    private String picture;

    @ManyToOne(optional = false)
    @NotNull
    private UnitOfMeasure uom;

    @ManyToOne(optional = false)
    private UnitOfMeasure uomWeight;

    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @NotNull
    private ProductCategory category;

    @ManyToMany
    @JoinTable(
        name = "rel_product__system_model",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "system_model_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"system", "products"}, allowSetters = true)
    private Set<SystemModel> systemModels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Product code(String code) {
        this.setCode(code);
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public BigDecimal getWeight() {
        return this.weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Product weight(BigDecimal weight) {
        this.setWeight(weight);
        return this;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Product picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public UnitOfMeasure getUom() {
        return this.uom;
    }

    public void setUom(UnitOfMeasure unitOfMeasure) {
        this.uom = unitOfMeasure;
    }

    public Product uom(UnitOfMeasure unitOfMeasure) {
        this.setUom(unitOfMeasure);
        return this;
    }

    public UnitOfMeasure getUomWeight() {
        return this.uomWeight;
    }

    public void setUomWeight(UnitOfMeasure unitOfMeasure) {
        this.uomWeight = unitOfMeasure;
    }

    public Product uomWeight(UnitOfMeasure unitOfMeasure) {
        this.setUomWeight(unitOfMeasure);
        return this;
    }

    public ProductCategory getCategory() {
        return this.category;
    }

    public void setCategory(ProductCategory productCategory) {
        this.category = productCategory;
    }

    public Product category(ProductCategory productCategory) {
        this.setCategory(productCategory);
        return this;
    }

    public Set<SystemModel> getSystemModels() {
        return this.systemModels;
    }

    public void setSystemModels(Set<SystemModel> systemModels) {
        this.systemModels = systemModels;
    }

    public Product systemModels(Set<SystemModel> systemModels) {
        this.setSystemModels(systemModels);
        return this;
    }

    public Product addSystemModel(SystemModel systemModel) {
        this.systemModels.add(systemModel);
        systemModel.getProducts().add(this);
        return this;
    }

    public Product removeSystemModel(SystemModel systemModel) {
        this.systemModels.remove(systemModel);
        systemModel.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}
