package com.collinz.mobilemoney.requests;

import com.collinz.mobilemoney.models.enums.ChargeType;
import com.collinz.mobilemoney.models.enums.Timeframe;
import com.collinz.mobilemoney.validators.ValueOfEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferLimitRequest {
    @NotNull
    private Integer currencyId;
    @NotNull
    @ValueOfEnum(enumClass = Timeframe.class)
    private String timeframe;
    @Min(0)
    private BigDecimal amount;

    public TransferLimitRequest() {
    }

    public TransferLimitRequest(Integer currencyId, String timeframe, BigDecimal amount) {
        this.currencyId = currencyId;
        this.timeframe = timeframe;
        this.amount = amount;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
