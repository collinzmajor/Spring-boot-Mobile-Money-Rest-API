package com.collinz.mobilemoney.requests;

import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.models.enums.ChargeType;
import com.collinz.mobilemoney.models.enums.FeeType;
import com.collinz.mobilemoney.validators.ValueOfEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ChargeRequest {
    @NotEmpty
    private String name;
    @Min(0)
    private BigDecimal minimumAmount;
    @Min(0)
    private BigDecimal maximumAmount;
    @NotNull
    @ValueOfEnum(enumClass = ChargeType.class)
    private String chargeType;
    @NotNull
    @ValueOfEnum(enumClass = FeeType.class)
    private String feeType;
    @Min(0)
    private BigDecimal value;
    @NotNull
    private Integer currencyId;

    public ChargeRequest() {
    }

    public ChargeRequest(String name, BigDecimal minimumAmount, BigDecimal maximumAmount, String chargeType, String feeType, BigDecimal value, Integer currencyId) {
        this.name = name;
        this.minimumAmount = minimumAmount;
        this.maximumAmount = maximumAmount;
        this.chargeType = chargeType;
        this.feeType = feeType;
        this.value = value;
        this.currencyId = currencyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }
}
