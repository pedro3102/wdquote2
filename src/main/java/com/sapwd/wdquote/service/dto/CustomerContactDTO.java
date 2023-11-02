package com.sapwd.wdquote.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.CustomerContact} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerContactDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String code;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 20)
    private String phone;

    @Size(max = 50)
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;

    @Size(max = 25)
    private String salespersonCode;

    @NotNull
    private Boolean isDefaultContact;

    private CustomerDTO customer;

    private LanguageDTO language;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalespersonCode() {
        return salespersonCode;
    }

    public void setSalespersonCode(String salespersonCode) {
        this.salespersonCode = salespersonCode;
    }

    public Boolean getIsDefaultContact() {
        return isDefaultContact;
    }

    public void setIsDefaultContact(Boolean isDefaultContact) {
        this.isDefaultContact = isDefaultContact;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public LanguageDTO getLanguage() {
        return language;
    }

    public void setLanguage(LanguageDTO language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerContactDTO)) {
            return false;
        }

        CustomerContactDTO customerContactDTO = (CustomerContactDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerContactDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerContactDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", salespersonCode='" + getSalespersonCode() + "'" +
            ", isDefaultContact='" + getIsDefaultContact() + "'" +
            ", customer=" + getCustomer() +
            ", language=" + getLanguage() +
            "}";
    }
}
