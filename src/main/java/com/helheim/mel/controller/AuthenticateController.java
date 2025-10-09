package com.helheim.mel.controller;

import com.helheim.mel.form.AccountForm;
import com.helheim.mel.service.AuthenticateService;
import com.helheim.mel.service.RegistrationService;
import com.helheim.mel.util.DailyCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AuthenticateController {
    private final AuthenticateService authService;
    private final RegistrationService registerService;

    public AuthenticateController(AuthenticateService authService, RegistrationService registerService) {
        this.authService = authService;
        this.registerService = registerService;

    }

    @ModelAttribute
    public void addForms(Model model){
        if(!model.containsAttribute("AccountForm")) {
            model.addAttribute("AccountForm", new AccountForm());
        }
        if(!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new AccountForm());
        }
    }

    @GetMapping
    public String loginPage(Model model) {
        return "home/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("AccountForm") AccountForm form, Model model) {
        if (authService.authenticate(form.getUsernameOrEmail(), form.getPassword())) {
            return "home/homepage";
        } else {
            model.addAttribute("registerForm", new AccountForm());
            model.addAttribute("loginErrorMessage", "ユーザ名もしくはパスワードが違います。");
            model.addAttribute("AccountForm", form);
            return "home/login";
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerForm") AccountForm form, @RequestParam String checkPassword, @RequestParam String dailyCode, Model model) {
        int error = 0;

        if (form.getUsername() == null || form.getUsername().isEmpty()) {
            model.addAttribute("usernameError", "ユーザー名が空です。");
            error++;
        }
        if (form.getPassword() == null || form.getPassword().isEmpty()) {
            model.addAttribute("passwordError", "パスワードが空白です。");
            error++;
        }
        if (checkPassword == null || checkPassword.isEmpty() || !(checkPassword.equals(form.getPassword()))) {
            model.addAttribute("checkPasswordError", "再入力したパスワードが一致しません。");
            error++;
        }
        if (form.getEmail() == null || form.getEmail().isEmpty() || !form.getEmail().contains("@")) {
            model.addAttribute("emailError", "メールアドレスが存在しません。");
            error++;
        }
        if (authService.emailExists(form.getEmail())) {
            model.addAttribute("emailError", "メールアドレスが既に存在します");
            error++;
        }
        if (dailyCode == null || dailyCode.isEmpty() || !dailyCode.equals(DailyCode.getTokenToday())) {
            model.addAttribute("dailyCodeError", "コードが違います。");
        }
        if (form.getPhone().length() > 11){
            model.addAttribute("phoneError", "電話番号は11桁にしてください");
            error++;
        }
        if (error > 0){
            model.addAttribute("showRegisterModal", true);
            model.addAttribute("loginErrorMessage", "");
            model.addAttribute("registerErrorMessage", "アカウント作成に失敗しました。");
            return "home/login";
        }
        registerService.registerAccount(form);
        return "home/login";
    }
}
