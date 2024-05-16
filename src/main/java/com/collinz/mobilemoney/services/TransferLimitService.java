package com.collinz.mobilemoney.services;

import com.collinz.mobilemoney.exceptions.ObjectAlreadyExistsException;
import com.collinz.mobilemoney.exceptions.ObjectNotFoundException;
import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.models.TransferLimit;
import com.collinz.mobilemoney.models.enums.Status;
import com.collinz.mobilemoney.models.enums.Timeframe;
import com.collinz.mobilemoney.models.keys.TransferLimitId;
import com.collinz.mobilemoney.repositories.TransferLimitRepository;
import com.collinz.mobilemoney.requests.ChargeRequest;
import com.collinz.mobilemoney.requests.TransferLimitRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransferLimitService {
    private final TransferLimitRepository repository;
    private final CurrencyService currencyService;

    public TransferLimitService(TransferLimitRepository transferLimitRepository, CurrencyService currencyService){
        this.repository = transferLimitRepository;
        this.currencyService = currencyService;
    }

    public List<TransferLimit> findAllTransferLimits(){
        return repository.findAll();
    }

    public TransferLimit findById(Integer currencyId, String timeframeStr) throws ObjectNotFoundException {
        Timeframe timeframe = getTimeframe(timeframeStr);
        TransferLimitId id = new TransferLimitId(currencyId, timeframe);
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Transfer Limit not found!"));
    }

    public void save(TransferLimitRequest transferLimitRequest) throws ObjectNotFoundException{
        Currency currency = currencyService.findById(transferLimitRequest.getCurrencyId());
        Timeframe timeframe = getTimeframe(transferLimitRequest.getTimeframe());

        TransferLimitId id = new TransferLimitId(transferLimitRequest.getCurrencyId(), timeframe);

        Optional<TransferLimit> transferLimitOptional = repository.findById(id);
        TransferLimit transferLimit;

        if (transferLimitOptional.isPresent()){
            //update
            transferLimit = transferLimitOptional.get();
        }
        else{
            transferLimit = new TransferLimit();
            transferLimit.setTransferLimitId(id);
            transferLimit.setStatus(Status.ACTIVE);
            transferLimit.setCurrency(currency);
            transferLimit.setCreatedAt(LocalDateTime.now());
        }
        transferLimit.setAmount(transferLimitRequest.getAmount());

        repository.save(transferLimit);
    }

    private Timeframe getTimeframe(String timeframeStr) throws ObjectNotFoundException{
        Timeframe timeframe;

        try{
            timeframe = Timeframe.valueOf(timeframeStr.toUpperCase());
        }
        catch (IllegalArgumentException ex){
            throw new ObjectNotFoundException("Transfer limit not found!");
        }

        return timeframe;
    }

    @Transactional
    public void toggleStatus(Integer currencyId, String timeframe) throws ObjectNotFoundException{
        TransferLimit transferLimit = this.findById(currencyId, timeframe);
        transferLimit.setStatus((transferLimit.getStatus() == Status.ACTIVE)? Status.DISABLED : Status.ACTIVE);
    }
}
