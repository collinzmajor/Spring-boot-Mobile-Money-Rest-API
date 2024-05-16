package com.collinz.mobilemoney.repositories;

import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.models.TransferLimit;
import com.collinz.mobilemoney.models.keys.TransferLimitId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferLimitRepository extends JpaRepository<TransferLimit, TransferLimitId> {
    List<TransferLimit> findAllByCurrency(Currency currency);
}
