package com.helheim.mel.service;

import com.helheim.mel.entity.AccountEntity;
import com.helheim.mel.form.AccountForm;
import com.helheim.mel.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final AccountRepository accountRepository;

    public RegistrationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void registerAccount(AccountForm accountForm) {
        AccountEntity account = new AccountEntity();

        account.setUsername(accountForm.getUsername());
        account.setPassword(accountForm.getPassword());
        account.setEmail(accountForm.getEmail());
        account.setPhone(accountForm.getPhone());
        account.setAddress(accountForm.getAddress());

        accountRepository.save(account);
    }
}
