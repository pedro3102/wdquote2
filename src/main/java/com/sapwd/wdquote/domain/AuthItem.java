package com.sapwd.wdquote.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AuthItem.
 */
@Entity
@Table(name = "auth_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AuthItem extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_item_id_seq")
    @SequenceGenerator(name = "auth_item_id_seq", sequenceName = "auth_item_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "item_type", nullable = false)
    private Integer type;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parent")
    private List<AuthItemChild> children = new ArrayList<>();

    public AuthItem() {}

    public AuthItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AuthItemChild> getChildren() {
        return children;
    }

    public void setChildren(List<AuthItemChild> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AuthItem authItem = (AuthItem) o;
        return id != null && Objects.equals(id, authItem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
