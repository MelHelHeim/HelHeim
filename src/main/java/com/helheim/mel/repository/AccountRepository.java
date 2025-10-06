package com.helheim.mel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.helheim.mel.entity.AccountEntity;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByUsername(String username);
    Optional<AccountEntity> findByEmail(String email);
}
