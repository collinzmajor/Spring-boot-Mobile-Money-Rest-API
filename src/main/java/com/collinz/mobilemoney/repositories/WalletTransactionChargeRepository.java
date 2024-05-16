package com.collinz.mobilemoney.repositories;

import com.collinz.mobilemoney.models.WalletTransactionCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionChargeRepository extends JpaRepository<WalletTransactionCharge, Long> {
}
