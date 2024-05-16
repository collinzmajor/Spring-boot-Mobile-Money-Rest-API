package com.collinz.mobilemoney.requests;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class WalletTransferRequest {
    @Min(0)
    private Long recipientId;
    @Min(0)
    private Integer currencyId;
    @Min(0)
    private BigDecimal amount;

    public WalletTransferRequest(Long recipientId, Integer currencyId, BigDecimal amount) {
        this.recipientId = recipientId;
        this.currencyId = currencyId;
        this.amount = amount;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long receipientId) {
        this.recipientId = receipientId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
