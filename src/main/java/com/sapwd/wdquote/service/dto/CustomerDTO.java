package com.sapwd.wdquote.service.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.sapwd.wdquote.domain.Customer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String code;

    @NotNull
    @Size(max = 50)
    private String name;

    private String codeName;

    @Size(max = 255)
    private String address;

    @NotNull
    private Boolean taxLiable;

    @Size(max = 25)
    private String taxExemptionCode;

    @Size(max = 25)
    private String paymentTerms;

    private BigDecimal creditLimit;

    @NotNull
    private Boolean blocked;

    private DeliveryZoneDTO deliveryZone;

    private TaxAreaCodeDTO taxAreaCode;

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

    public Boolean getTaxLiable() {
        return taxLiable;
    }

    public void setTaxLiable(Boolean taxLiable) {
        this.taxLiable = taxLiable;
    }

    public String getTaxExemptionCode() {
        return taxExemptionCode;
    }

    public void setTaxExemptionCode(String taxExemptionCode) {
        this.taxExemptionCode = taxExemptionCode;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public DeliveryZoneDTO getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(DeliveryZoneDTO deliveryZone) {
        this.deliveryZone = deliveryZone;
    }

    public TaxAreaCodeDTO getTaxAreaCode() {
        return taxAreaCode;
    }

    public void setTaxAreaCode(TaxAreaCodeDTO taxAreaCode) {
        this.taxAreaCode = taxAreaCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerDTO)) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", taxLiable='" + getTaxLiable() + "'" +
            ", taxExemptionCode='" + getTaxExemptionCode() + "'" +
            ", paymentTerms='" + getPaymentTerms() + "'" +
            ", creditLimit=" + getCreditLimit() +
            ", blocked='" + getBlocked() + "'" +
            ", deliveryZone=" + getDeliveryZone() +
            ", taxAreaCode=" + getTaxAreaCode() +
            "}";
    }
}
