package com.sapwd.wdquote.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer extends AbstractAuditingEntity<Long> implements Serializable {

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
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Formula(value = "concat(code, ' - ', name)")
    private String codeName;

    @Size(max = 255)
    @Column(name = "address", length = 255)
    private String address;

    @NotNull
    @Column(name = "tax_liable", nullable = false)
    private Boolean taxLiable;

    @Size(max = 25)
    @Column(name = "tax_exemption_code", length = 25)
    private String taxExemptionCode;

    @Size(max = 25)
    @Column(name = "payment_terms", length = 25)
    private String paymentTerms;

    @Column(name = "credit_limit", precision = 21, scale = 2)
    private BigDecimal creditLimit;

    @NotNull
    @Column(name = "blocked", nullable = false)
    private Boolean blocked;

    @ManyToOne(optional = false)
    @NotNull
    private DeliveryZone deliveryZone;

    @ManyToOne(optional = false)
    @NotNull
    private TaxAreaCode taxAreaCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Customer code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Customer name(String name) {
        this.setName(name);
        return this;
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
        return this.address;
    }

    public Customer address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getTaxLiable() {
        return this.taxLiable;
    }

    public Customer taxLiable(Boolean taxLiable) {
        this.setTaxLiable(taxLiable);
        return this;
    }

    public void setTaxLiable(Boolean taxLiable) {
        this.taxLiable = taxLiable;
    }

    public String getTaxExemptionCode() {
        return this.taxExemptionCode;
    }

    public Customer taxExemptionCode(String taxExemptionCode) {
        this.setTaxExemptionCode(taxExemptionCode);
        return this;
    }

    public void setTaxExemptionCode(String taxExemptionCode) {
        this.taxExemptionCode = taxExemptionCode;
    }

    public String getPaymentTerms() {
        return this.paymentTerms;
    }

    public Customer paymentTerms(String paymentTerms) {
        this.setPaymentTerms(paymentTerms);
        return this;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public BigDecimal getCreditLimit() {
        return this.creditLimit;
    }

    public Customer creditLimit(BigDecimal creditLimit) {
        this.setCreditLimit(creditLimit);
        return this;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Boolean getBlocked() {
        return this.blocked;
    }

    public Customer blocked(Boolean blocked) {
        this.setBlocked(blocked);
        return this;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public DeliveryZone getDeliveryZone() {
        return this.deliveryZone;
    }

    public void setDeliveryZone(DeliveryZone deliveryZone) {
        this.deliveryZone = deliveryZone;
    }

    public Customer deliveryZone(DeliveryZone deliveryZone) {
        this.setDeliveryZone(deliveryZone);
        return this;
    }

    public TaxAreaCode getTaxAreaCode() {
        return this.taxAreaCode;
    }

    public void setTaxAreaCode(TaxAreaCode taxAreaCode) {
        this.taxAreaCode = taxAreaCode;
    }

    public Customer taxAreaCode(TaxAreaCode taxAreaCode) {
        this.setTaxAreaCode(taxAreaCode);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", taxLiable='" + getTaxLiable() + "'" +
            ", taxExemptionCode='" + getTaxExemptionCode() + "'" +
            ", paymentTerms='" + getPaymentTerms() + "'" +
            ", creditLimit=" + getCreditLimit() +
            ", blocked='" + getBlocked() + "'" +
            "}";
    }
}
