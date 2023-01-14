package com.example.fusionwallet.repository;

import com.example.fusionwallet.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance,Long> {
}
