package com.example.fusionwallet.controller;

import com.example.fusionwallet.model.*;
import com.example.fusionwallet.service.BalanceService;
import com.example.fusionwallet.service.UserService;
import com.example.fusionwallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;

    @PostMapping("/create/{user_id}")
    public ResponseEntity<?> createWallet(@PathVariable Long user_id){
        try {
            Optional<User> createdUser = userService.findbyId(user_id);
            if (createdUser.isPresent()){
                Wallet createWallet = walletService.createWallet(createdUser.get());
                return new ResponseEntity<>(createWallet.walletToDto(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/retrieve/{wallet_id}")
    public ResponseEntity<?> getWallet(@PathVariable Long wallet_id){
        try {
            Optional<Wallet> foundWallet = walletService.findById(wallet_id);
            if (foundWallet.isPresent()){
                return new ResponseEntity<>(foundWallet.get().walletToDto(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/retrieveAll/{user_id}")
    public ResponseEntity<?> getAllWallet(@PathVariable Long user_id){
        try {
            List<Wallet> foundWallets = walletService.findAllByUserId(user_id);
            if (!(foundWallets.size() == 0)){
                return new ResponseEntity<>(foundWallets.stream().map(
                        wallet -> wallet.walletToDto()).toList(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}