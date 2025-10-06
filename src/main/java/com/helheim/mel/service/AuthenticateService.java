package com.helheim.mel.service;

import com.helheim.mel.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {
    private final AccountRepository accountRepository;

    public AuthenticateService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public boolean authenticate(String usernameOrEmail, String password) {
        return accountRepository.findByUsername(usernameOrEmail)
                .or(() -> accountRepository.findByEmail(usernameOrEmail))
                .map(account -> account.getPassword().equals(password))
                .orElse(false);
    }
    public void changePassword(String username, String oldPassword, String newPassword) {
        //later
    }
    public void changeUsername(String username, String newUsername) {
        //later
    }
}
