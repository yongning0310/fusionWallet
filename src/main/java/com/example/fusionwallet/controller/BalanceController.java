package com.example.fusionwallet.controller;


import com.example.fusionwallet.model.User;
import com.example.fusionwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/balances")
public class BalanceController {
    @Autowired
    private UserService userService;

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getBalance(@RequestParam String currency, @PathVariable Long user_id) {
        try {
            Optional<User> foundUser = userService.findbyId(user_id);
            if (foundUser.isPresent()) {
                return new ResponseEntity<>(foundUser.get().getBalances().get(0).getBalance(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
