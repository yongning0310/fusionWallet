package com.example.fusionwallet.controller;


import com.example.fusionwallet.model.User;
import com.example.fusionwallet.model.Wallet;
import com.example.fusionwallet.service.UserService;
import com.example.fusionwallet.service.WalletService;
import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.constant.Currency;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/balances")
public class BalanceController {
    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionController transactionController;

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getBalance(@RequestParam String currency, @PathVariable Long user_id) {
        try {
            Optional<User> foundUser = userService.findbyId(user_id);
            if (foundUser.isPresent()) {
                double balance = 0;
    
                // get cash balance
                double cashBalance = foundUser.get().getBalances().get(0).getBalance();
                if (Objects.equals(currency, "cash")) {
                    return new ResponseEntity<>(cashBalance, HttpStatus.OK);
                } else {
                    balance += cashBalance;
                }

                // get eth balance
                Wallet foundWallets = walletService.findAllByUserId(user_id).get(0);
                Web3j web3 = Web3j.build(new HttpService("https://eth-goerli.g.alchemy.com/v2/HrX79z1T6WqOYQWPivmEvr4ZJbtqoGcz"));
                EthGetBalance ethGetBalance = web3
                        .ethGetBalance(foundWallets.getPublicKey(), DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();
                BigDecimal wei = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
                double ethBalance = wei.doubleValue();
                if (Objects.equals(currency, "eth")) {
                    return new ResponseEntity<>(ethBalance, HttpStatus.OK);
                } else {
                    CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
                    double eth_price = client.getPrice("ethereum", Currency.USD).get("ethereum").get(Currency.USD);
                    balance += wei.doubleValue() * eth_price;
                }

                if (Objects.equals(currency, "all")) {
                    return new ResponseEntity<>(balance, HttpStatus.OK);
                }

            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // only return wallet when calling wallet API
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
