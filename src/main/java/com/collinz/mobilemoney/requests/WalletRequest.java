package com.collinz.mobilemoney.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WalletRequest {
    @Min(0)
    private Integer currencyId;

    public WalletRequest(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }
}
