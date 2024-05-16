package com.collinz.mobilemoney.repositories;

import com.collinz.mobilemoney.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    public Optional<Currency> findByCode(String code);

    public boolean existsByCode(String code);
}
