package com.collinz.mobilemoney.services;

import com.collinz.mobilemoney.exceptions.*;
import com.collinz.mobilemoney.models.*;
import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.models.enums.ChargeType;
import com.collinz.mobilemoney.models.enums.FeeType;
import com.collinz.mobilemoney.models.enums.Status;
import com.collinz.mobilemoney.models.enums.Timeframe;
import com.collinz.mobilemoney.models.keys.WalletTransactionChargeId;
import com.collinz.mobilemoney.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private final WalletRepository repository;
    private final ChargeRepository chargeRepository;
    private final TransferLimitRepository limitRepository;
    private final WalletTransactionRepository transactionRepository;
    private final CurrencyService currencyService;
    private final UserService userService;

    @Autowired
    public WalletService(WalletRepository walletRepository, UserService userService, ChargeRepository chargeRepository, CurrencyService currencyService, TransferLimitRepository limitRepository, WalletTransactionRepository transactionRepository){
        this.repository = walletRepository;
        this.chargeRepository = chargeRepository;
        this.limitRepository = limitRepository;
        this.transactionRepository = transactionRepository;
        this.currencyService = currencyService;
        this.userService = userService;
    }

    //get list of all wallets
    public List<Wallet> findAllWallets(){
        return repository.findAll();
    }

    //get list of wallets by user
    public List<Wallet> findMyWallets(Long userId) throws ObjectNotFoundException{
        User user = userService.findUserById(userId);
        return repository.findAllWalletsByUser(user);
    }

    public Wallet findMyWallet(Long userId, Integer currencyId) throws ObjectNotFoundException {
        Currency currency = currencyService.findById(currencyId);
        User user = userService.findUserById(userId);
        return repository.findWalletByCurrencyAndUser(currency, user).orElseThrow(() -> new ObjectNotFoundException("Wallet not found!"));
    }

    //get list of all wallets by currency
    public List<Wallet> findAllWalletsByCurrency(Currency currency){
        return repository.findAllWalletsByCurrency(currency);
    }

    //get wallet by id
    public Wallet findWalletById(Long id) throws ObjectNotFoundException {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Wallet not found!"));
    }

    //get wallet by user and Currency
    public Wallet findWalletByCurrencyAndUser(User user, Currency currency) throws ObjectNotFoundException{
        return repository.findWalletByCurrencyAndUser(currency, user).orElseThrow(() -> new ObjectNotFoundException("Wallet not found!"));
    }

    //create wallet
    public Wallet insert(Long userId, Integer currencyId) throws ObjectAlreadyExistsException, ObjectNotFoundException{
        User user = userService.findUserById(userId);
        Currency currency = currencyService.findById(currencyId);
        if (repository.existsByCurrencyAndUser(currency, user)) throw  new ObjectAlreadyExistsException("Wallet already exists!");

        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("1000.00"));
        wallet.setCurrency(currency);
        wallet.setUser(user);
        wallet.setStatus(Status.ACTIVE);
        wallet.setCreatedAt(LocalDateTime.now());

        wallet = repository.saveAndFlush(wallet);

        return wallet;
    }

    //transfer
    @Transactional
    public Map<String, Object> transfer(Long senderId, Long recipientId, Integer currencyId, BigDecimal amount) throws InsufficientBalanceException, LimitExceededException, ObjectNotFoundException, InvalidTransactionException {
        if(senderId.equals(recipientId))throw new InvalidTransactionException("You cannot send money to yourself!");

        Currency currency = currencyService.findById(currencyId);
        Wallet sendingWallet = this.getWallet(senderId, currencyId);
        Wallet receivingWallet = this.getWallet(recipientId, currencyId);
        BigDecimal chargeAmount = chargeRepository.getTotalChargesFormAmount(currency.getId(), amount, ChargeType.TRANSACTION.toString());
        BigDecimal deductionAmount = amount.add(chargeAmount);
        Map<String, Object> result = new HashMap<>();
        List<TransferLimit> limits;
        TransferLimit limit;
        BigDecimal totalAmount;
        LocalDate today = LocalDate.now();
        LocalDateTime fromDate;
        List<WalletTransactionCharge> walletTransactionCharges = new ArrayList<>();
        LocalDateTime toDate = LocalDateTime.of(today, LocalTime.MIDNIGHT.minusSeconds(1));
        //get charges
        List<Charge> charges;

        //Does this wallet have enough balance
        if (sendingWallet.getBalance().compareTo(deductionAmount) == -1){
            throw new InsufficientBalanceException("You have an insufficient balance to make this transaction!");
        }

        //Does this user exceed limits for this account
        limits = limitRepository.findAllByCurrency(currency);
        //1. check transactional limit
        limit = getTransferLimitForPeriod(limits, Timeframe.TRANSACTION);
        if (limit != null && limit.getAmount().compareTo(amount) == -1){
            throw new LimitExceededException("This transaction exceeds the transaction limit!");
        }

        //2. check daily limit
        limit = getTransferLimitForPeriod(limits, Timeframe.DAY);
        if(limit != null) {
            fromDate = LocalDateTime.of(today, LocalTime.MIDNIGHT);
            totalAmount = transactionRepository.getTotalSentUsingRange(sendingWallet, fromDate, toDate);
            if (totalAmount == null)totalAmount = new BigDecimal("0");
            if (limit.getAmount().compareTo(totalAmount) == -1){
                throw new LimitExceededException("This transaction exceeds the daily limit!");
            }
        }

        //3. check weekly limit
        limit = getTransferLimitForPeriod(limits, Timeframe.WEEK);
        if(limit != null) {
            fromDate = LocalDateTime.of(today.with(DayOfWeek.SUNDAY), LocalTime.MIDNIGHT);
            totalAmount = transactionRepository.getTotalSentUsingRange(sendingWallet, fromDate, toDate);
            if (totalAmount == null)totalAmount = new BigDecimal("0");
            if (limit.getAmount().compareTo(totalAmount) == -1){
                throw new LimitExceededException("This transaction exceeds the weekly limit!");
            }
        }

        //4. check monthly limit
        limit = getTransferLimitForPeriod(limits, Timeframe.MONTH);
        if(limit != null) {
            fromDate = LocalDateTime.of(LocalDate.of(today.getYear(), today.getMonth(), 1), LocalTime.MIDNIGHT);
            totalAmount = transactionRepository.getTotalSentUsingRange(sendingWallet, fromDate, toDate);
            if (totalAmount == null)totalAmount = new BigDecimal("0");
            if (limit.getAmount().compareTo(totalAmount) == -1){
                throw new LimitExceededException("This transaction exceeds the monthly limit!");
            }
        }

        //we are good to make the transfer
        //save transaction
        WalletTransaction transaction = new WalletTransaction();
        transaction.setAmount(amount);
        transaction.setChargeAmount(chargeAmount);
        transaction.setCurrency(currency);
        transaction.setSender(sendingWallet);
        transaction.setRecipient(receivingWallet);
        transaction.setCreatedAt(LocalDateTime.now());
//        transaction = transactionRepository.saveAndFlush(transaction);

        charges = chargeRepository.findAllChargesForAmount(currency, amount, ChargeType.TRANSACTION);
        charges.forEach((charge -> {
            WalletTransactionCharge tc = new WalletTransactionCharge();
            tc.setWalletTransactionChargeId(new WalletTransactionChargeId(null, charge.getId()));
            tc.setAmount(charge.getValue());
            tc.setCharge(charge);
            tc.setWalletTransaction(transaction);
            tc.setCharge(charge);
            walletTransactionCharges.add(tc);
        }));

        transaction.setCharges(walletTransactionCharges);
        transactionRepository.saveAndFlush(transaction);

        //update balance
        sendingWallet.setBalance(sendingWallet.getBalance().subtract(deductionAmount));
        receivingWallet.setBalance(receivingWallet.getBalance().add(amount));

        result.put("wallet", sendingWallet);
        result.put("transaction", transaction);
        return result;
    }

    private TransferLimit getTransferLimitForPeriod(List<TransferLimit> limits, Timeframe timeframe){
        List<TransferLimit> result = limits.stream()
                .filter(item -> item.getTransferLimitId().getTimeframe() == timeframe)
                .collect(Collectors.toList());

        return (result.isEmpty())? null : result.get(0);
    }

    private Wallet getWallet(Long userId, Integer currencyId) throws ObjectNotFoundException{
        User user = userService.findUserById(userId);
        Currency currency = currencyService.findById(currencyId);
        Wallet wallet;
        Optional<Wallet> walletOptional = repository.findWalletByCurrencyAndUser(currency, user);
        if (walletOptional.isPresent()){
            wallet = walletOptional.get();
        }
        else{

            try {
                wallet = this.insert(userId, currencyId);
            }
            catch(ObjectAlreadyExistsException ex){
                //do nothing because this won't happen
                wallet = repository.findWalletByCurrencyAndUser(currency, user).orElse(null);
            }


        }

        return wallet;
    }
}
