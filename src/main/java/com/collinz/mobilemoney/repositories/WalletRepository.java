package com.collinz.mobilemoney.repositories;

import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.models.User;
import com.collinz.mobilemoney.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findAllWalletsByUser(User user);
    List<Wallet> findAllWalletsByCurrency(Currency currency);
    Optional<Wallet> findWalletByCurrencyAndUser(Currency currency, User user);
    boolean existsByCurrencyAndUser(Currency currency, User user);
}
