package com.example.fusionwallet.service;

import com.example.fusionwallet.model.User;
import com.example.fusionwallet.model.Wallet;
import com.example.fusionwallet.model.WalletDto;
import com.example.fusionwallet.repository.UserRepository;
import com.example.fusionwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public Wallet createWallet(User user){
        String privateKey = "to be completed. private key";
        String publicKey = "to be completed. public key";
        Wallet walletToCreate = new Wallet();
        walletToCreate.setUser(user);
        walletToCreate.setPrivateKey(privateKey);
        walletToCreate.setPublicKey(publicKey);
        return walletRepository.saveAndFlush(walletToCreate);
    }

    public Optional<Wallet> findById(Long wallet_id){
        return walletRepository.findById(wallet_id);
    }

    public List<Wallet> findAllByUserId(Long wallet_id){
        return walletRepository.findByUser_IdOrderByIdAsc(wallet_id);
    }
}
