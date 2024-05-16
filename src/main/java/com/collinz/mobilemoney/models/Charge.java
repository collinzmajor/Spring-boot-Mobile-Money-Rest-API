package com.collinz.mobilemoney.models;

import com.collinz.mobilemoney.models.enums.ChargeType;
import com.collinz.mobilemoney.models.enums.Status;
import com.collinz.mobilemoney.models.enums.FeeType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Charge {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false,precision = 20, scale = 2)
    private BigDecimal minimumAmount;
    @Column(precision = 20, scale = 2)
    private BigDecimal maximumAmount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChargeType chargeType;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeeType feeType;
    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal value;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    public Charge() {
    }

    public Charge(Integer id, String name, Currency currency, BigDecimal minimumAmount, BigDecimal maximumAmount, ChargeType chargeType, FeeType feeType, BigDecimal value, Status status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.feeType = feeType;
        this.chargeType = chargeType;
        this.value = value;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public BigDecimal getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(BigDecimal maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currency=" + currency +
                ", minimumAmount=" + minimumAmount +
                ", maximumAmount=" + maximumAmount +
                ", feeType=" + feeType +
                ", chargeType=" + chargeType +
                ", value=" + value +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}

