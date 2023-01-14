package com.example.fusionwallet.repository;

import com.example.fusionwallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
    List<Wallet> findByUser_IdOrderByIdAsc(Long id);

}
