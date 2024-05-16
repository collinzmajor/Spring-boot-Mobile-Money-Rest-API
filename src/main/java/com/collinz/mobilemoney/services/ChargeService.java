package com.collinz.mobilemoney.services;

import com.collinz.mobilemoney.exceptions.ObjectAlreadyExistsException;
import com.collinz.mobilemoney.exceptions.ObjectNotFoundException;
import com.collinz.mobilemoney.models.Charge;
import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.models.enums.ChargeType;
import com.collinz.mobilemoney.models.enums.FeeType;
import com.collinz.mobilemoney.models.enums.Status;
import com.collinz.mobilemoney.repositories.ChargeRepository;
import com.collinz.mobilemoney.requests.ChargeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChargeService {
    private final ChargeRepository chargeRepository;
    private final CurrencyService currencyService;

    @Autowired
    public ChargeService(ChargeRepository chargeRepository, CurrencyService currencyService){
        this.chargeRepository = chargeRepository;
        this.currencyService = currencyService;
    }

    public List<Charge> findAllChargesByCurrencyId(Integer currencyId){
        return chargeRepository.getAllChargesByCurrencyId(currencyId);
    }

    public Charge findById(Integer id) throws ObjectNotFoundException {
        return chargeRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Charge not found!"));
    }

    public void insert(ChargeRequest chargeRequest) throws ObjectAlreadyExistsException, ObjectNotFoundException{
        //make sure the currency exists
        Currency currency = currencyService.findById(chargeRequest.getCurrencyId());

        //make sure there are no conflicts
        if (chargeRepository.existsByCurrencyAndName(currency, chargeRequest.getName()))throw new ObjectAlreadyExistsException("A charge with the specified name already exists!");

        Charge charge = new Charge();
        charge.setName(chargeRequest.getName());
        charge.setCurrency(currency);
        charge.setMaximumAmount(chargeRequest.getMaximumAmount());
        charge.setMinimumAmount(chargeRequest.getMinimumAmount());
        charge.setChargeType(ChargeType.valueOf(chargeRequest.getChargeType().toUpperCase()));
        charge.setFeeType(FeeType.valueOf(chargeRequest.getFeeType().toUpperCase()));
        charge.setValue(chargeRequest.getValue());
        charge.setStatus(Status.ACTIVE);
        charge.setCreatedAt(LocalDateTime.now());

        chargeRepository.save(charge);
    }

//    private boolean hasConflicts(ChargeRequest chargeRequest) {
//        List<Charge> conflicts = (chargeRequest.getMaximumAmount() == null)
//                                    ?
//                                        chargeRepository.findAllConflicts(chargeRequest.getCurrency(), chargeRequest.getChargeType(), chargeRequest.getMinimumAmount())
//                                    :
//                                        chargeRepository.findAllConflicts(chargeRequest.getCurrency(), chargeRequest.getChargeType(), chargeRequest.getMinimumAmount(), chargeRequest.getMaximumAmount());
//        return !conflicts.isEmpty();
//    }

    @Transactional
    public void update(Integer id, ChargeRequest chargeRequest) throws ObjectNotFoundException, ObjectAlreadyExistsException{
        Charge charge = this.findById(id);

        if (chargeRequest.getName() != null && !chargeRequest.getName().equals(charge.getName())){
            if (chargeRepository.existsByCurrencyAndName(charge.getCurrency(), charge.getName())) throw new ObjectAlreadyExistsException("A charge with the specified name already exists!");
            charge.setName(chargeRequest.getName());
        }

        ChargeType chargeType = ChargeType.valueOf(chargeRequest.getChargeType().toUpperCase());
        if (chargeRequest.getChargeType() != null && !chargeType.equals(charge.getChargeType())){
            charge.setChargeType(chargeType);
        }

        FeeType feeType = FeeType.valueOf(chargeRequest.getFeeType().toUpperCase());
        if (chargeRequest.getFeeType() != null && !feeType.equals(charge.getFeeType())){
            charge.setFeeType(feeType);
        }

        if (chargeRequest.getMinimumAmount() != null && !chargeRequest.getMinimumAmount().equals(charge.getMinimumAmount())){
            charge.setMinimumAmount(chargeRequest.getMinimumAmount());
        }

        if (chargeRequest.getMaximumAmount() != null && !chargeRequest.getMaximumAmount().equals(charge.getMaximumAmount())){
            charge.setMaximumAmount(chargeRequest.getMaximumAmount());
        }

        if (chargeRequest.getValue() != null && !chargeRequest.getValue().equals(charge.getValue())){
            charge.setValue(chargeRequest.getValue());
        }
    }

    @Transactional
    public void toggleStatus(Integer id) throws ObjectNotFoundException{
        Charge charge =  this.findById(id);
        charge.setStatus(
                (charge.getStatus() == Status.ACTIVE)? Status.DISABLED : Status.ACTIVE
        );
    }
}
