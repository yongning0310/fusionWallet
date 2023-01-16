package com.example.fusionwallet.controller;

import com.example.fusionwallet.model.User;
import com.example.fusionwallet.service.BalanceService;
import com.example.fusionwallet.service.UserService;
import com.example.fusionwallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/retrieve/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable Long user_id) {
        try {
            Optional<User> foundUser = userService.findbyId(user_id);
            if (foundUser.isPresent()) {
                return new ResponseEntity<>(foundUser.get().userDto(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/retrieveByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> foundUser = userService.findbyEmail(email);
            if (foundUser.isPresent()) {
                return new ResponseEntity<>(foundUser.get().userDto(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // only return wallet when calling wallet API
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User userDetails) {
        try {
            User createdUser = userService.createAcc(userDetails);
            balanceService.createCashBalance(createdUser);
            // only return wallet when calling wallet API
            return new ResponseEntity<>(createdUser.userDto(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
