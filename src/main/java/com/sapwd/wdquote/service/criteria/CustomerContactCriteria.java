package com.sapwd.wdquote.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.sapwd.wdquote.domain.CustomerContact} entity. This class is used
 * in {@link com.sapwd.wdquote.web.rest.CustomerContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customer-contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerContactCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter phone;

    private StringFilter email;

    private StringFilter salespersonCode;

    private BooleanFilter isDefaultContact;

    private LongFilter customerId;

    private StringFilter customer;

    private LongFilter languageId;

    private StringFilter language;

    private StringFilter global;

    private Boolean distinct;

    public CustomerContactCriteria() {}

    public CustomerContactCriteria(CustomerContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.salespersonCode = other.salespersonCode == null ? null : other.salespersonCode.copy();
        this.isDefaultContact = other.isDefaultContact == null ? null : other.isDefaultContact.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.customer = other.customer == null ? null : other.customer.copy();
        this.languageId = other.languageId == null ? null : other.languageId.copy();
        this.language = other.language == null ? null : other.language.copy();
        this.distinct = other.distinct;
        this.global = other.global;
    }

    @Override
    public CustomerContactCriteria copy() {
        return new CustomerContactCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getSalespersonCode() {
        return salespersonCode;
    }

    public StringFilter salespersonCode() {
        if (salespersonCode == null) {
            salespersonCode = new StringFilter();
        }
        return salespersonCode;
    }

    public void setSalespersonCode(StringFilter salespersonCode) {
        this.salespersonCode = salespersonCode;
    }

    public BooleanFilter getIsDefaultContact() {
        return isDefaultContact;
    }

    public BooleanFilter isDefaultContact() {
        if (isDefaultContact == null) {
            isDefaultContact = new BooleanFilter();
        }
        return isDefaultContact;
    }

    public void setIsDefaultContact(BooleanFilter isDefaultContact) {
        this.isDefaultContact = isDefaultContact;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getCustomer() {
        return customer;
    }

    public void setCustomer(StringFilter customer) {
        this.customer = customer;
    }

    public LongFilter getLanguageId() {
        return languageId;
    }

    public LongFilter languageId() {
        if (languageId == null) {
            languageId = new LongFilter();
        }
        return languageId;
    }

    public void setLanguageId(LongFilter languageId) {
        this.languageId = languageId;
    }

    public StringFilter getLanguage() {
        return language;
    }

    public void setLanguage(StringFilter language) {
        this.language = language;
    }

    public StringFilter getGlobal() {
        return global;
    }

    public void setGlobal(StringFilter global) {
        this.global = global;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerContactCriteria that = (CustomerContactCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(salespersonCode, that.salespersonCode) &&
            Objects.equals(isDefaultContact, that.isDefaultContact) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(customer, that.customer) &&
            Objects.equals(languageId, that.languageId) &&
            Objects.equals(language, that.language) &&
            Objects.equals(global, that.global) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, phone, email, salespersonCode, isDefaultContact, customerId, languageId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerContactCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (salespersonCode != null ? "salespersonCode=" + salespersonCode + ", " : "") +
            (isDefaultContact != null ? "isDefaultContact=" + isDefaultContact + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (customer != null ? "customer=" + customer + ", " : "") +
            (languageId != null ? "languageId=" + languageId + ", " : "") +
            (language != null ? "language=" + language + ", " : "") +
            (global != null ? "global=" + global + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
