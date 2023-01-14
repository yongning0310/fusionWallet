package com.example.fusionwallet.service;

import com.example.fusionwallet.model.Balance;
import com.example.fusionwallet.model.User;
import com.example.fusionwallet.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    public void createCashBalance(User user)throws Exception{
        Balance cashAcc = new Balance();

        cashAcc.setUser(user);
        cashAcc.setCurrency("Cash");
        cashAcc.setBalance(0);
        balanceRepository.save(cashAcc);
    }
}
