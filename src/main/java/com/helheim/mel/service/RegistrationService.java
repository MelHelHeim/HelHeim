package com.helheim.mel.service;

import com.helheim.mel.entity.AccountEntity;
import com.helheim.mel.form.AccountForm;
import com.helheim.mel.repository.AccountRepository;
import com.helheim.mel.util.DailyCode;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class RegistrationService {
    private final AccountRepository accountRepository;
    private final AuthenticateService authService;

    public RegistrationService(AccountRepository accountRepository, AuthenticateService authService) {
        this.accountRepository = accountRepository;
        this.authService = authService;
    }

    public Map<String, String> register(AccountForm form, String checkPassword, String dailyCode) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (form.getUsername() == null || form.getUsername().isEmpty()) {
            errors.put("usernameError", "ユーザー名が空です。");
        }
        if (form.getPassword() == null || form.getPassword().isEmpty()) {
            errors.put("passwordError", "パスワードが空白です。");
        }
        if (checkPassword == null || checkPassword.isEmpty() || !(checkPassword.equals(form.getPassword()))) {
            errors.put("checkPasswordError", "再入力したパスワードが一致しません。");
        }
        if (form.getEmail() == null || form.getEmail().isEmpty() || !form.getEmail().contains("@")) {
            errors.put("emailError", "メールアドレスが存在しません。");
        }
        if (authService.emailExists(form.getEmail())) {
            errors.put("emailError", "メールアドレスが既に存在します");
        }
        if (dailyCode == null || dailyCode.isEmpty() || !dailyCode.equals(DailyCode.getTokenToday())) {
            errors.put("dailyCodeError", "コードが違います。");
        }
        if (form.getPhone() != null && !form.getPhone().isEmpty()){
            if (form.getPhone().length() != 11) {
                errors.put("phoneError", "電話番号は11桁にしてください");
            }
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        registerAccount(form);
        return errors;
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
