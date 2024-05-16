package com.collinz.mobilemoney.models.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WalletTransactionChargeId implements Serializable {
    @Column(name = "wallet_transaction_id", nullable = false)
    private Long walletTransactionId;
    @Column(name="charge_id", nullable = false)
    private Integer chargeId;

    public WalletTransactionChargeId() {
    }

    public WalletTransactionChargeId(Long walletTransactionId, Integer chargeId) {
        this.walletTransactionId = walletTransactionId;
        this.chargeId = chargeId;
    }

    public Long getWalletTransactionId() {
        return walletTransactionId;
    }

    public void setWalletTransactionId(Long walletTransactionId) {
        this.walletTransactionId = walletTransactionId;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletTransactionChargeId that = (WalletTransactionChargeId) o;
        return Objects.equals(walletTransactionId, that.walletTransactionId) && Objects.equals(chargeId, that.chargeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletTransactionId, chargeId);
    }
}
