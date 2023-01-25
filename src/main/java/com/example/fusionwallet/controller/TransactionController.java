package com.example.fusionwallet.controller;

import com.example.fusionwallet.model.Transaction;
import com.example.fusionwallet.service.TransactionService;
import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.constant.Currency;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/cryptoPrices/{crypto_id}")
    public ResponseEntity<?> getCryptoPrices(@PathVariable String crypto_id) {
        try {
            CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
            Map<String, Map<String, Double>> coin = client.getPrice(crypto_id, Currency.USD);
            double price = coin.get(crypto_id).get(Currency.USD);
            return new ResponseEntity<>(price, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // trsf, get all logs
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam Long from_user_id, Long to_user_id,
                                      @RequestBody Transaction transaction) {
        try {
            double price = (double) getCryptoPrices("ethereum").getBody();
            if (transactionService.transfer(from_user_id, to_user_id, transaction, price)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/depositCash/{user_id}/{amount}")
    public ResponseEntity<?> depositCash(@RequestParam Long user_id,
                                         double amount) {
        try {
            if (transactionService.depositCash(user_id,amount)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/withdrawCash/{user_id}/{amount}")
    public ResponseEntity<?> withdrawCash(@PathVariable Long user_id,
                                          double amount) {
        try {
            if (transactionService.withdrawCash(user_id,amount)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/withdrawCrypto/{user_id}/{amount}")
    public ResponseEntity<?> withdrawCrypto(@PathVariable Long user_id,
                                            double amount) {
        try {
            double price = (double) getCryptoPrices("ethereum").getBody();
            if (transactionService.withdrawCrypto(user_id,amount,price)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/loan/{loan_amount}")
    public ResponseEntity<?> loan(@PathVariable double loan_amount,
                                  @RequestParam Long user_id) {
        try {
            double price = (double) getCryptoPrices("ethereum").getBody();
            if (transactionService.loan(user_id, loan_amount, price)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
