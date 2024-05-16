package com.collinz.mobilemoney.services;

import com.collinz.mobilemoney.exceptions.ObjectAlreadyExistsException;
import com.collinz.mobilemoney.exceptions.ObjectNotFoundException;
import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.models.enums.Status;
import com.collinz.mobilemoney.repositories.CurrencyRepository;
import com.collinz.mobilemoney.requests.CurrencyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }
    public List<Currency> findAllCurrencies(){
        return currencyRepository.findAll();
    }

    public Currency findById(Integer id) throws ObjectNotFoundException{
        return currencyRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Currency not found!"));
    }

    public void insert(CurrencyRequest currencyRequest) throws ObjectAlreadyExistsException{
        //make sure this code is not in use
        if(currencyRepository.existsByCode(currencyRequest.getCode()))throw new ObjectAlreadyExistsException("Currency already exists!");

        Currency currency = new Currency();
        currency.setCode(currencyRequest.getCode());
        currency.setName(currencyRequest.getName());
        currency.setSymbol(currencyRequest.getSymbol());
        currency.setStatus(Status.ACTIVE);
        currency.setCreatedAt(LocalDateTime.now());

        currencyRepository.save(currency);
    }

    @Transactional
    public void update(Integer id, CurrencyRequest currencyRequest) throws ObjectNotFoundException, ObjectAlreadyExistsException{
        Currency currency =  this.findById(id);

        if (currencyRequest.getCode() != null && !currencyRequest.getCode().equals(currency.getCode()) ){
            //make sure this code is unique
            if(currencyRepository.existsByCode(currencyRequest.getCode()))throw new ObjectAlreadyExistsException("Currency already exists!");

            currency.setCode(currencyRequest.getCode());
        }

        if (currencyRequest.getSymbol() != null && !currencyRequest.getSymbol().equals(currency.getSymbol())){
            currency.setSymbol(currencyRequest.getSymbol());
        }

        if (currencyRequest.getName() != null && !currencyRequest.getName().equals(currency.getName())){
            currency.setName(currencyRequest.getName());
        }
    }

    @Transactional
    public void toggleStatus(Integer id) throws ObjectNotFoundException{
        Currency currency =  this.findById(id);
        currency.setStatus(
                (currency.getStatus() == Status.ACTIVE)? Status.DISABLED : Status.ACTIVE
        );
    }
}
