package com.helheim.mel.controller;

import com.helheim.mel.form.AccountForm;
import com.helheim.mel.service.AuthenticateService;
import com.helheim.mel.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

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
public String login(@ModelAttribute("AccountForm") AccountForm form, Model model, HttpSession session) {
    if (authService.authenticate(form.getUsernameOrEmail(), form.getPassword())) {
        var account = authService.resolveAccount(form.getUsernameOrEmail());
        session.setAttribute("accountId", account.getId());
        session.setAttribute("username", account.getUsername());
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
    Map<String, String> errors = registerService.register(form, checkPassword, dailyCode);

    if (!errors.isEmpty()) {
        model.addAllAttributes(errors);
        model.addAttribute("showRegisterModal", true);
        model.addAttribute("loginErrorMessage");
        model.addAttribute("registerErrorMessage", "アカウント作成に失敗しました。");
        return "home/login";
    }
    return "home/login";
    }
}
