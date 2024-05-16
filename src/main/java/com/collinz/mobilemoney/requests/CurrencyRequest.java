package com.collinz.mobilemoney.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

public class CurrencyRequest {
    @NotEmpty
    @Size(min = 2, max = 6)
    private String code;
    @NotEmpty
    @Size(min=1, max=5)
    private String symbol;
    @NotEmpty
    @Size(min=5, max=100)
    private String name;

    public CurrencyRequest(String code, String symbol, String name) {
        this.code = code;
        this.symbol = symbol;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
