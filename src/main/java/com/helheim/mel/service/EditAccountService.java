package com.helheim.mel.service;

import com.helheim.mel.entity.AccountEntity;
import com.helheim.mel.form.AccountForm;
import com.helheim.mel.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EditAccountService {
    private final AuthenticateService authService;
    private final AccountRepository accountRepository;

    public EditAccountService(AuthenticateService authService, AccountRepository accountRepository) {
        this.authService = authService;
        this.accountRepository = accountRepository;
    }

    public void fillCurrentValues(AccountForm form, Long accountId) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("アカウントが存在しません。"));

        form.setUsername(account.getUsername());
        form.setEmail(account.getEmail());
        form.setPhone(account.getPhone());
        form.setAddress(account.getAddress());
        form.setPassword(account.getPassword());
    }

    public Map<String, String> edit(AccountForm form, String checkPassword, Long accountId) {
        Map<String, String> errors = new HashMap<>();

        if (form.getUsername() == null || form.getUsername().isBlank()) {
            errors.put("usernameError", "ユーザー名を入力してください。");
        }
        if (form.getEmail() == null || form.getEmail().isBlank()) {
            errors.put("emailError", "メールアドレスを入力してください。");
        }
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            errors.put("passwordError", "パスワードを入力してください。");
        }
        if (checkPassword == null || checkPassword.isBlank() || !checkPassword.equals(form.getPassword())) {
            errors.put("checkPasswordError", "パスワードと違います。");
        }
        if (form.getPhone() != null && !form.getPhone().isEmpty()) {
            if (!form.getPhone().matches("\\d{11}")) {
                errors.put("phoneError", "電話番号は11桁にしてください");
            }
        }
        if (!errors.isEmpty()) {
            return errors;
        }
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Unexpected: logged-in account not found"));

        if (authService.emailExists(form.getEmail()) && !form.getEmail().equals(account.getEmail())) {
            errors.put("emailError", "メールアドレスが既に存在します");
            return errors;
        }

        account.setUsername(form.getUsername());
        account.setEmail(form.getEmail());
        account.setPassword(form.getPassword());
        account.setPhone(form.getPhone());
        account.setAddress(form.getAddress());

        try {
            accountRepository.save(account);
        } catch (Exception e) {
            errors.put("editErrorMessage", "更新に失敗しました。");
        }
        return errors;
    }
}
