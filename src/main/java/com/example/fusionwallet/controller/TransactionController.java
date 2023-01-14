package com.example.fusionwallet.controller;

import com.example.fusionwallet.model.Transaction;
import com.example.fusionwallet.model.User;
import com.example.fusionwallet.model.Wallet;
import com.example.fusionwallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/cryptoPrices/{crypto_id}")
    public ResponseEntity<?> getCryptoPrices(@PathVariable Long crypto_id){
        try {
                String coinGeckoApi = "insertapi";
                String response = "coingecko json";
            if(true){ // successful API call
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    // trsf, get all logs
    @PostMapping("/transfer/{from_user_id}/{to_user_id}")
    public ResponseEntity<?> transfer(@PathVariable Long from_user_id, Long to_user_id,
                                      @RequestBody Transaction transaction){
        try {
            if (transactionService.transfer(from_user_id,to_user_id,transaction)){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
