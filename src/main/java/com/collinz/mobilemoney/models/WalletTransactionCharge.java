package com.collinz.mobilemoney.models;

import com.collinz.mobilemoney.models.keys.WalletTransactionChargeId;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class WalletTransactionCharge {
    @EmbeddedId
    private WalletTransactionChargeId walletTransactionChargeId;
    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal amount;
    @ManyToOne
    @MapsId("chargeId")
    @JoinColumn(name="charge_id")
    private Charge charge;
    @ManyToOne
    @MapsId("walletTransactionId")
    @JoinColumn(name="wallet_transaction_id")
    private WalletTransaction walletTransaction;

    public WalletTransactionCharge(WalletTransactionChargeId walletTransactionChargeId, BigDecimal amount) {
        this.walletTransactionChargeId = walletTransactionChargeId;
        this.amount = amount;
    }

    public WalletTransactionCharge() {
    }

    public WalletTransactionCharge(WalletTransactionChargeId walletTransactionChargeId) {
        this.walletTransactionChargeId = walletTransactionChargeId;
    }

    public WalletTransactionChargeId getWalletTransactionChargeId() {
        return walletTransactionChargeId;
    }

    public void setWalletTransactionChargeId(WalletTransactionChargeId walletTransactionChargeId) {
        this.walletTransactionChargeId = walletTransactionChargeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public WalletTransaction getWalletTransaction() {
        return walletTransaction;
    }

    public void setWalletTransaction(WalletTransaction walletTransaction) {
        this.walletTransaction = walletTransaction;
    }

    @Override
    public String toString() {
        return "WalletTransactionCharge{" +
                "walletTransactionChargeId=" + walletTransactionChargeId +
                ", amount=" + amount +
                ", charge=" + charge +
                ", walletTransaction=" + walletTransaction +
                '}';
    }
}
