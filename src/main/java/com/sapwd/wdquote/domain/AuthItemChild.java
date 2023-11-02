package com.sapwd.wdquote.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AuthItemChild.
 */
@Entity
@Table(name = "auth_item_child")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AuthItemChild extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_item_child_id_seq")
    @SequenceGenerator(name = "auth_item_child_id_seq", sequenceName = "auth_item_child_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private AuthItem parent;

    @ManyToOne
    private AuthItem child;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthItem getParent() {
        return parent;
    }

    public void setParent(AuthItem parent) {
        this.parent = parent;
    }

    public AuthItem getChild() {
        return child;
    }

    public void setChild(AuthItem child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthItemChild authItemChild = (AuthItemChild) o;
        if (authItemChild.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, authItemChild.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
