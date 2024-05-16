package com.collinz.mobilemoney.controllers;

import com.collinz.mobilemoney.exceptions.*;
import com.collinz.mobilemoney.models.User;
import com.collinz.mobilemoney.models.Wallet;
import com.collinz.mobilemoney.requests.WalletRequest;
import com.collinz.mobilemoney.requests.WalletTransferRequest;
import com.collinz.mobilemoney.services.UserService;
import com.collinz.mobilemoney.services.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class WalletController {
    private final WalletService service;

    @Autowired
    public WalletController(WalletService walletService){
        this.service = walletService;
    }

    @GetMapping
    public ResponseEntity<List<Wallet>> findAllWallets(){
        return ResponseEntity.ok(service.findAllWallets());
    }

    @GetMapping("/my/wallets")
    public ResponseEntity<List<Wallet>> findMyWallets(@AuthenticationPrincipal User user) throws ObjectNotFoundException {
        return ResponseEntity.ok(service.findMyWallets(user.getId()));
    }

    @GetMapping("/my/wallets/{currencyId}")
    public ResponseEntity<Wallet> findWallet(@PathVariable("currencyId") Integer currencyId, @AuthenticationPrincipal User user) throws ObjectNotFoundException{
        return ResponseEntity.ok(service.findMyWallet(user.getId(), currencyId));
    }

    @PostMapping("/my/wallets")
    public ResponseEntity<Wallet> findMyWallet(@RequestBody @Valid WalletRequest walletRequest) throws ObjectNotFoundException, ObjectAlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(1L, walletRequest.getCurrencyId()));
    }

    @PostMapping("/my/wallet/transfer")
    public ResponseEntity<Map<String, Object>> walletTransfer(@RequestBody @Valid WalletTransferRequest walletTransferRequest) throws ObjectNotFoundException, InsufficientBalanceException, LimitExceededException, InvalidTransactionException {
        return ResponseEntity.ok(service.transfer(1L, walletTransferRequest.getRecipientId(), walletTransferRequest.getCurrencyId(), walletTransferRequest.getAmount()));
    }
}
