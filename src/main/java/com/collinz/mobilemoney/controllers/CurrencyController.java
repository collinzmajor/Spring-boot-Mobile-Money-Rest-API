package com.collinz.mobilemoney.controllers;

import com.collinz.mobilemoney.exceptions.ObjectAlreadyExistsException;
import com.collinz.mobilemoney.exceptions.ObjectNotFoundException;
import com.collinz.mobilemoney.models.Currency;
import com.collinz.mobilemoney.requests.CurrencyRequest;
import com.collinz.mobilemoney.services.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService){
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<Currency>> findAllCurrencies(){
        return ResponseEntity.ok(currencyService.findAllCurrencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> findById(@PathVariable("id") Integer id) throws ObjectNotFoundException{
        return ResponseEntity.ok(currencyService.findById(id));
    }

    @PostMapping
    public ResponseEntity saveCurrency(@RequestBody @Valid CurrencyRequest currencyRequest) throws ObjectAlreadyExistsException {
        currencyService.insert(currencyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Currency successfully created!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id, @RequestBody CurrencyRequest currencyRequest) throws ObjectNotFoundException, ObjectAlreadyExistsException{
        currencyService.update(id, currencyRequest);
        return ResponseEntity.ok("Currency successfully updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable("id") Integer id) throws ObjectNotFoundException{
        currencyService.toggleStatus(id);
        return ResponseEntity.ok("Currency status successfully updated");
    }
}
