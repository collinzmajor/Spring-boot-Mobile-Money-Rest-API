package com.collinz.mobilemoney.models.keys;

import com.collinz.mobilemoney.models.enums.Timeframe;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TransferLimitId implements Serializable {
    @Column(nullable = false, name = "currency_id")
    private Integer currencyId;
    @Column(nullable = false, name = "timeframe")
    @Enumerated(EnumType.STRING)
    private Timeframe timeframe;

    public TransferLimitId() {
    }

    public TransferLimitId(Integer currencyId, Timeframe timeframe) {
        this.currencyId = currencyId;
        this.timeframe = timeframe;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Timeframe getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(Timeframe timeframe) {
        this.timeframe = timeframe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferLimitId that = (TransferLimitId) o;
        return Objects.equals(currencyId, that.currencyId) && timeframe == that.timeframe;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyId, timeframe);
    }
}
