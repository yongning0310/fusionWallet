package com.example.fusionwallet.service;

import com.example.fusionwallet.model.Balance;
import com.example.fusionwallet.model.Transaction;
import com.example.fusionwallet.model.User;
import com.example.fusionwallet.model.Wallet;
import com.example.fusionwallet.repository.BalanceRepository;
import com.example.fusionwallet.repository.TransactionRepository;
import com.example.fusionwallet.repository.UserRepository;
import com.example.fusionwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    public Boolean transfer(Long from_user_id, Long to_user_id,
                            Transaction transaction, double price) throws Exception {
        // check if to and from user exist
        // check cash or crypto
        // check if trans valid ( check balance )
        // create single trans
        // update both balances
        Optional<User> to_user = userRepository.findById(to_user_id);
        Optional<User> from_user = userRepository.findById(from_user_id);
        if (to_user.isPresent() && from_user.isPresent()) {
            // Create credential for from user to sign transactions
            User from = from_user.get();
            Wallet foundWallet = walletRepository.findByUser_IdOrderByIdAsc(from.getId()).get(0);
            Credentials from_credential = Credentials.create(foundWallet.getPrivateKey());

            // Transfer ETH to reserve
            Web3j web3 = Web3j.build(new HttpService());  // defaults to http://localhost:8545/
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3, from_credential, "0xf39fd6e51aad88f6f4ce6ab8827279cfffb92266",
                    BigDecimal.valueOf(transaction.getAmount()), Convert.Unit.ETHER).send();
            if (!transactionReceipt.isStatusOK()) return false;

            // Increase balance of to user
            User to = to_user.get();
            Balance to_user_cash_acc = to.getBalances()
                    .stream().filter(balance ->
                            balance.getCurrency() == "Cash").toList().get(0);
            double balance = to_user_cash_acc.getBalance();
            balance += transaction.getAmount() * price;
            to_user_cash_acc.setBalance(balance);
            userRepository.save(to);
        }

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
