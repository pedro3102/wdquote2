package com.sapwd.wdquote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A SystemModel.
 */
@Entity
@Table(name = "system_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemModel extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @Column(name = "picture")
    private String picture;

    @ManyToOne(optional = false)
    @NotNull
    private Systema system;

    @ManyToMany(mappedBy = "systemModels", cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {"uom", "uomWeight", "category", "systemModels"}, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SystemModel id(Long id) {
        this.setId(id);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SystemModel name(String name) {
        this.setName(name);
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SystemModel description(String description) {
        this.setDescription(description);
        return this;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public SystemModel picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public Systema getSystem() {
        return this.system;
    }

    public void setSystem(Systema systema) {
        this.system = systema;
    }

    public SystemModel system(Systema systema) {
        this.setSystem(systema);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.removeSystemModel(this));
        }
        if (products != null) {
            products.forEach(i -> i.addSystemModel(this));
        }
        this.products = products;
    }

    public SystemModel products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public SystemModel addProduct(Product product) {
        this.products.add(product);
        product.getSystemModels().add(this);
        return this;
    }

    public SystemModel removeProduct(Product product) {
        this.products.remove(product);
        product.getSystemModels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemModel)) {
            return false;
        }
        return id != null && id.equals(((SystemModel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemModel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}
