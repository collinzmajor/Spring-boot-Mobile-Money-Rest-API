package com.collinz.mobilemoney.models;


import com.collinz.mobilemoney.models.enums.Status;
import com.collinz.mobilemoney.models.keys.TransferLimitId;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class TransferLimit {
    @EmbeddedId
    private TransferLimitId transferLimitId;
    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    LocalDateTime createdAt;
    @ManyToOne
    @MapsId("currencyId")
    @JoinColumn(name = "currency_id")
    private Currency currency;

    public TransferLimit() {
    }

    public TransferLimit(TransferLimitId transferLimitId, BigDecimal amount, Status status, LocalDateTime createdAt, Currency currency) {
        this.transferLimitId = transferLimitId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.currency = currency;
    }

    public TransferLimitId getTransferLimitId() {
        return transferLimitId;
    }

    public void setTransferLimitId(TransferLimitId transferLimitId) {
        this.transferLimitId = transferLimitId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        return "TransferLimit{" +
                "transferLimitId=" + transferLimitId +
                ", amount=" + amount +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
