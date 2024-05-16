package com.collinz.mobilemoney.converters;

import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CurrencyByIdConverter implements Converter<Integer, Currency> {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyByIdConverter(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Currency convert(Integer id){
        return currencyRepository.findById(id).orElse(null);
    }
}
