package com.example.fusionwallet.repository;

import com.example.fusionwallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
