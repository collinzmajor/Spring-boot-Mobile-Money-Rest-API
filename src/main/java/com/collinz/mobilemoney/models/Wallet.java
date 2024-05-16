package com.collinz.mobilemoney.models;

import com.collinz.mobilemoney.models.enums.Status;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Wallet {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal balance;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    public Wallet(Long id, BigDecimal balance, Status status, LocalDateTime createdAt, User user, Currency currency) {
        this.id = id;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
        this.user = user;
        this.currency = currency;
    }

    public Wallet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", balance=" + balance +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", user=" + user +
                ", currency=" + currency +
                '}';
    }
}
