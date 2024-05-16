package com.collinz.mobilemoney.repositories;

import com.collinz.mobilemoney.models.Charge;
import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.models.enums.ChargeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Integer> {
    public List<Charge> getAllChargesByCurrencyId(Integer currencyId);

    @Query("" +
            "SELECT " +
            "   c " +
            "FROM " +
            "   Charge c " +
            "WHERE " +
            "   c.currency=?1 " +
            "       AND " +
            "   c.chargeType=?2 " +
            "       AND " +
            "   (" +
            "       (c.minimumAmount <= ?3 AND (c.maximumAmount IS NULL OR c.maximumAmount >= ?3)) " +
            "           OR " +
            "       (c.minimumAmount >= ?3 AND c.minimumAmount <= ?4) " +
            "   )"
    )
    List<Charge> findAllConflicts(Currency currency, String chargeType, BigDecimal minimum, BigDecimal maximum);

    @Query("" +
            "SELECT " +
            "   c " +
            "FROM " +
            "   Charge c " +
            "WHERE " +
            "   c.currency=?1 " +
            "       AND " +
            "   c.chargeType=?2 " +
            "       AND " +
            "   ("+
            "       c.maximumAmount >= ?3 " +
            "           OR " +
            "       c.maximumAmount is null" +
            "   )"
    )
    List<Charge> findAllConflicts(Currency currency, String chargeType, BigDecimal minimum);

    boolean existsByCurrencyAndName(Currency currency, String name);

    @Query("" +
            "SELECT c FROM Charge c WHERE " +
            "   c.chargeType = ?3 AND " +
            "   c.currency = ?1 AND " +
            "   c.minimumAmount <= ?2 AND " +
            "   (" +
            "       c.maximumAmount is null " +
            "           OR " +
            "       c.maximumAmount >= ?2" +
            "   )"
    )
    List<Charge> findAllChargesForAmount(Currency currency, BigDecimal amount, ChargeType chargeType);

    @Query(value = "" +
            "SELECT SUM(" +
            "           CASE " +
            "               WHEN fee_type='FIXED'" +
            "                   THEN value " +
            "               ELSE (value / 100) * ?2 " +
            "           END" +
            "       )" +
            "FROM charge " +
            "WHERE" +
            "   charge_type = ?3 AND currency_id = ?1 AND " +
            "   minimum_amount <= ?2 AND " +
            "   (" +
            "       maximum_amount is null " +
            "           OR " +
            "       maximum_amount >= ?2" +
            "   )", nativeQuery = true
    )
    BigDecimal getTotalChargesFormAmount(Integer currencyId, BigDecimal amount, String chargeType);
}
