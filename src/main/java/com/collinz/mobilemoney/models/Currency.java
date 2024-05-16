package com.collinz.mobilemoney.models;

import com.collinz.mobilemoney.models.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Currency {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false, unique = true, length = 6)
    private String code;
    @Column(nullable = false, length = 3)
    private String symbol;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    LocalDateTime createdAt;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(
            mappedBy = "currency",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE}
    )
    private List<Charge> charges = new ArrayList<>();

    @OneToMany(mappedBy = "currency")
    private List<Wallet> wallets = new ArrayList<>();
    @OneToMany(
            mappedBy = "currency",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE}
    )
    private List<TransferLimit> limits = new ArrayList<>();

    public Currency() {
    }

    public Currency(Integer id, String code, String symbol, String name, LocalDateTime createdAt, Status status) {
        this.id = id;
        this.code = code;
        this.symbol = symbol;
        this.name = name;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                ", charges=" + charges +
                '}';
    }

    public List<Charge> getCharges() {
        return charges;
    }

    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public List<TransferLimit> getLimits() {
        return limits;
    }

    public void setLimits(List<TransferLimit> limits) {
        this.limits = limits;
    }
}
