package com.collinz.mobilemoney.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WalletTransaction {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, precision = 20, scale = 2, updatable = false)
    private BigDecimal amount;
    @Column(nullable = false, precision = 20, scale = 2, updatable = false)
    private BigDecimal chargeAmount;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Wallet sender;
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Wallet recipient;
    @OneToMany(mappedBy = "walletTransaction", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    List<WalletTransactionCharge> charges = new ArrayList<>();

    public WalletTransaction() {
    }

    public WalletTransaction(long id, BigDecimal amount, LocalDateTime createdAt, Currency currency, BigDecimal chargeAmount, Wallet sender, Wallet recipient) {
        this.id = id;
        this.amount = amount;
        this.createdAt = createdAt;
        this.currency = currency;
        this.sender = sender;
        this.recipient = recipient;
        this.chargeAmount = chargeAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Wallet getSender() {
        return sender;
    }

    public void setSender(Wallet sender) {
        this.sender = sender;
    }

    public Wallet getRecipient() {
        return recipient;
    }

    public void setRecipient(Wallet recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "WalletTransaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", currency=" + currency +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", chargeAmount=" + chargeAmount +
                '}';
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public List<WalletTransactionCharge> getCharges() {
        return charges;
    }

    public void setCharges(List<WalletTransactionCharge> charges) {
        this.charges = charges;
    }
}
