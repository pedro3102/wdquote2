package com.sapwd.wdquote.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CustomerContact.
 */
@Entity
@Table(name = "customer_contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerContact extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "code", length = 10, nullable = false)
    private String code;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 50)
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    @Column(name = "email", length = 50)
    private String email;

    @Size(max = 25)
    @Column(name = "salesperson_code", length = 25)
    private String salespersonCode;

    @NotNull
    @Column(name = "is_default_contact", nullable = false)
    private Boolean isDefaultContact;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "deliveryZone", "taxAreaCode" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(optional = false)
    @NotNull
    private Language language;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerContact id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public CustomerContact code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public CustomerContact name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public CustomerContact phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public CustomerContact email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalespersonCode() {
        return this.salespersonCode;
    }

    public CustomerContact salespersonCode(String salespersonCode) {
        this.setSalespersonCode(salespersonCode);
        return this;
    }

    public void setSalespersonCode(String salespersonCode) {
        this.salespersonCode = salespersonCode;
    }

    public Boolean getIsDefaultContact() {
        return this.isDefaultContact;
    }

    public CustomerContact isDefaultContact(Boolean isDefaultContact) {
        this.setIsDefaultContact(isDefaultContact);
        return this;
    }

    public void setIsDefaultContact(Boolean isDefaultContact) {
        this.isDefaultContact = isDefaultContact;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerContact customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public CustomerContact language(Language language) {
        this.setLanguage(language);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerContact)) {
            return false;
        }
        return id != null && id.equals(((CustomerContact) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerContact{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", salespersonCode='" + getSalespersonCode() + "'" +
            ", isDefaultContact='" + getIsDefaultContact() + "'" +
            "}";
    }
}
