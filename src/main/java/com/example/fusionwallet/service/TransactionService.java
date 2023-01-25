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
import java.time.LocalDateTime;
import java.util.Objects;
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
            Credentials from_credential = getUserCredentials(from);

            // Transfer ETH to reserve
            TransactionReceipt transactionReceipt = transferEth(from_credential, transaction.getAmount());
            if (!transactionReceipt.isStatusOK()) return false;

            // Increase balance of to user
            User to = to_user.get();
            Balance to_user_cash_acc = to.getBalances()
                    .stream().filter(balance ->
                            Objects.equals(balance.getCurrency(), "Cash")).toList().get(0);
            double balance = to_user_cash_acc.getBalance();
            balance += transaction.getAmount() * price;
            to_user_cash_acc.setBalance(balance);
            userRepository.save(to);
            transaction.setDate(LocalDateTime.now());
            transaction.setFrom_user(from_user.get());
            transaction.setTo_user(to_user.get());
            transactionRepository.save(transaction);
            return true;
        }
        return false;

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

    public Boolean loan(Long id, double amount, double eth_price) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            // Create credential for from user to sign transactions
            User from = user.get();
            Credentials from_credential = getUserCredentials(from);

            // Transfer ETH to reserve
            TransactionReceipt transactionReceipt = transferEth(from_credential, amount);
            if (!transactionReceipt.isStatusOK()) return false;

            // Increment user balance
            Balance balance = user.get().getBalances()
                    .stream().filter(bal ->
                            Objects.equals(bal.getCurrency(), "Cash")).toList().get(0);
            balance.setBalance(balance.getBalance() + amount * eth_price * 0.5);
            balanceRepository.save(balance);
            Transaction newtrans = new Transaction();
            newtrans.setAmount(amount);
            newtrans.setDate(LocalDateTime.now());
            newtrans.setType("Loan");
            newtrans.setFrom_user(user.get());
            newtrans.setFromCurrency("ETH");
            newtrans.setToCurrency("Cash");
            transactionRepository.save(newtrans);
            return true;
        }
        return false;
    }

    private Credentials getUserCredentials(User user) {
        Wallet foundWallet = walletRepository.findByUser_IdOrderByIdAsc(user.getId()).get(0);
        return Credentials.create(foundWallet.getPrivateKey());
    }

    private TransactionReceipt transferEth(Credentials from, double amount) throws Exception {
        Web3j web3 = Web3j.build(new HttpService("https://eth-goerli.g.alchemy.com/v2/HrX79z1T6WqOYQWPivmEvr4ZJbtqoGcz"));  // defaults to http://localhost:8545/
        return Transfer.sendFunds(
                web3, from, "0x55A577185A249B7730712949171502eE9792A848",
                BigDecimal.valueOf(amount), Convert.Unit.ETHER).send();
    }

    public Boolean withdrawCash(Long id,double amount){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Balance balance = user.get().getBalances()
                    .stream().filter(bal ->
                            Objects.equals(bal.getCurrency(), "Cash")).toList().get(0);
            balance.setBalance(balance.getBalance() - amount);
            balanceRepository.save(balance);
            Transaction newtrans = new Transaction();
            newtrans.setAmount(amount);
            newtrans.setDate(LocalDateTime.now());
            newtrans.setType("Withdraw in Cash");
            newtrans.setFrom_user(user.get());
            newtrans.setToCurrency("Cash");
            transactionRepository.save(newtrans);
            return true;
        }
        return false;
    }

    public Boolean withdrawCrypto(Long id, double amount, double eth_price){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Balance balance = user.get().getBalances()
                    .stream().filter(bal ->
                            Objects.equals(bal.getCurrency(), "Cash")).toList().get(0);
            balance.setBalance(balance.getBalance() - amount *  eth_price);
            balanceRepository.save(balance);
            Transaction newtrans = new Transaction();
            newtrans.setAmount(amount);
            newtrans.setDate(LocalDateTime.now());
            newtrans.setType("Withdraw in Cash");
            newtrans.setFrom_user(user.get());
            newtrans.setToCurrency("Cash");
            transactionRepository.save(newtrans);
            return true;
        }
        return false;
    }

    public Boolean depositCash(Long id, double amount){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Balance balance = user.get().getBalances()
                    .stream().filter(bal ->
                            Objects.equals(bal.getCurrency(), "Cash")).toList().get(0);
            balance.setBalance(balance.getBalance() + amount);
            balanceRepository.save(balance);
            Transaction newtrans = new Transaction();
            newtrans.setAmount(amount);
            newtrans.setDate(LocalDateTime.now());
            newtrans.setType("Deposit in Cash");
            newtrans.setFrom_user(user.get());
            newtrans.setToCurrency("Cash");
            transactionRepository.save(newtrans);
            return true;
        }
        return false;
    }

}
