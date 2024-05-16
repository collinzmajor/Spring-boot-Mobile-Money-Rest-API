package com.collinz.mobilemoney.controllers;

import com.collinz.mobilemoney.exceptions.ObjectAlreadyExistsException;
import com.collinz.mobilemoney.exceptions.ObjectNotFoundException;
import com.collinz.mobilemoney.models.Charge;
import com.collinz.mobilemoney.repositories.ChargeRepository;
import com.collinz.mobilemoney.requests.ChargeRequest;
import com.collinz.mobilemoney.services.ChargeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charge")
public class ChargeController {
    private final ChargeService chargeService;

    public ChargeController(ChargeService chargeService){
        this.chargeService = chargeService;
    }

    @GetMapping("/currency/{currencyId}")
    public ResponseEntity<List<Charge>> findAllChargesByCurrencyId(@PathVariable("currencyId") Integer currencyId){
        return ResponseEntity.ok(chargeService.findAllChargesByCurrencyId(currencyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Charge> findById(@PathVariable("id") Integer id) throws ObjectNotFoundException {
        return ResponseEntity.ok(chargeService.findById(id));
    }

    @PostMapping
    public ResponseEntity saveCharge(@RequestBody @Valid ChargeRequest chargeRequest) throws ObjectAlreadyExistsException, ObjectNotFoundException {
        chargeService.insert(chargeRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("Charge successfully created!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateCharge(@PathVariable("id") Integer id, @RequestBody @Valid ChargeRequest chargeRequest) throws ObjectAlreadyExistsException, ObjectNotFoundException{
        chargeService.update(id, chargeRequest);

        return ResponseEntity.ok("Charge successfully updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity changeStatus(@PathVariable("id") Integer id) throws ObjectNotFoundException{
        chargeService.toggleStatus(id);

        return ResponseEntity.ok("Charge status successfully updated!");
    }
}
