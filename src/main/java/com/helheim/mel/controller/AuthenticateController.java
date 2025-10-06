package com.helheim.mel.controller;

import com.helheim.mel.form.AccountForm;
import com.helheim.mel.service.AuthenticateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthenticateController {
    private final AuthenticateService authService;

    public AuthenticateController(AuthenticateService authService) {
        this.authService = authService;
    }

@GetMapping
public String loginPage(Model model){
        model.addAttribute("AccountForm", new AccountForm());
        return "home/login";
    }

@PostMapping
public String login(AccountForm form, Model model){
        if(authService.authenticate(form.getUsernameOrEmail(), form.getPassword())){
            return "home/homepage";
        }else{
            model.addAttribute("errorMessage", "ユーザ名もしくはパスワードが違います。");
            model.addAttribute("AccountForm", form);
            return "home/login";
        }
}
}
