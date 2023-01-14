package com.example.fusionwallet.service;

import com.example.fusionwallet.model.User;
import com.example.fusionwallet.model.Wallet;
import com.example.fusionwallet.repository.UserRepository;
import com.example.fusionwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public Wallet createWallet(User user) {
        Wallet walletToCreate = new Wallet();
        walletToCreate.setUser(user);
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            Credentials cs = Credentials.create(ecKeyPair);
            walletToCreate.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
            walletToCreate.setPublicKey(cs.getAddress());
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {

        }
        return walletRepository.saveAndFlush(walletToCreate);
    }

    public Optional<Wallet> findById(Long wallet_id) {
        return walletRepository.findById(wallet_id);
    }

    public List<Wallet> findAllByUserId(Long wallet_id) {
        return walletRepository.findByUser_IdOrderByIdAsc(wallet_id);
    }
}
