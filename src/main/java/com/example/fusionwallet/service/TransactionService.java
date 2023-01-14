package com.example.fusionwallet.service;

import com.example.fusionwallet.model.Balance;
import com.example.fusionwallet.model.Transaction;
import com.example.fusionwallet.model.User;
import com.example.fusionwallet.repository.BalanceRepository;
import com.example.fusionwallet.repository.TransactionRepository;
import com.example.fusionwallet.repository.UserRepository;
import com.example.fusionwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Boolean transfer(Long from_user_id, Long to_user_id,
                            Transaction transaction) throws Exception{
        // check if to and from user exist
        // check cash or crypto
        // check if trans valid ( check balance )
        // create single trans
        // update both balances
        Optional<User> to_user = userRepository.findById(to_user_id);
        Optional<User> from_user = userRepository.findById(from_user_id);

        return true;
//        if (to_user.isPresent() && from_user.isPresent()){
//
//            if(transaction.getType() == "Cash"){
//                // cash transfer
//                double new_amount = transaction.getAmount();
//                Balance from_user_cash_acc = from_user.get().getBalances()
//                                .stream().filter(balance ->
//                                    balance.getCurrency() =="Cash").toList().get(0);
//                double from_user_balance = from_user_cash_acc.getBalance();
//                if (from_user_balance+ new_amount > 0){
//                    // valid
//                }else{
//                    // insufficient Balance
//                }
//            }else{
//                // crypto transfer
//            }
//
//        }
//        return false;
    }
}
