package com.collinz.mobilemoney.controllers;

import com.collinz.mobilemoney.exceptions.ObjectNotFoundException;
import com.collinz.mobilemoney.models.TransferLimit;
import com.collinz.mobilemoney.requests.TransferLimitRequest;
import com.collinz.mobilemoney.services.TransferLimitService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfer-limit")
public class TransferLimitController {
    private final TransferLimitService service;

    @Autowired
    public TransferLimitController(TransferLimitService transferLimitService){
        this.service = transferLimitService;
    }

    @GetMapping
    public ResponseEntity<List<TransferLimit>> findAllTransferLimits(){
        return ResponseEntity.ok(service.findAllTransferLimits());
    }

    @GetMapping("/{currencyId}/{timeframe}")
    public ResponseEntity<TransferLimit> findById(@PathVariable("currencyId") Integer currencyId, @PathVariable("timeframe") String timeframe) throws ObjectNotFoundException {
        return ResponseEntity.ok(service.findById(currencyId, timeframe));
    }

    @PostMapping
    public ResponseEntity<String> saveTransferLimit(@RequestBody @Valid TransferLimitRequest transferLimitRequest) throws ObjectNotFoundException{
        service.save(transferLimitRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transfer Limit successfully saved");
    }

    @DeleteMapping("/{currencyId}/{timeframe}")
    public ResponseEntity changeStatus(@PathVariable("currencyId") Integer currencyId, @PathVariable("timeframe") String timeframe) throws ObjectNotFoundException {
        service.toggleStatus(currencyId, timeframe);
        return ResponseEntity.ok("Transfer limit status successfully updated!");
    }
}
