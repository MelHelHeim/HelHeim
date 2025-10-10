package com.helheim.mel.controller;

import com.helheim.mel.form.AccountForm;
import com.helheim.mel.service.EditAccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user-account")
public class AccountController {
    private final EditAccountService editAccountService;

    public AccountController(EditAccountService editAccountService) {
        this.editAccountService = editAccountService;
    }

@ModelAttribute
public void addForms(Model model){
    if(!model.containsAttribute("accountForm")){
        model.addAttribute("accountForm", new AccountForm());
    }
}

@GetMapping
public String accountPage(@ModelAttribute("accountForm") AccountForm form,
                          Model model, HttpSession session){
    Long accountId = (Long)session.getAttribute("accountId");
    editAccountService.fillCurrentValues(form, accountId);
    model.addAttribute("editPage", false);
    return "account/userAccount";
    }

@PostMapping("/edit")
public String editAccountPage(@ModelAttribute("accountForm") AccountForm form,
                              Model model, HttpSession session){
    Long accountId = (Long)session.getAttribute("accountId");
    editAccountService.fillCurrentValues(form, accountId);
    model.addAttribute("editPage", true);
    return "account/userAccount";
}

@PostMapping("/input")
public String checkEditAccountPage(@ModelAttribute("accountForm") AccountForm form, Model model,
                                   @RequestParam String checkPassword, HttpSession session){
    Long  accountId = (Long)session.getAttribute("accountId");
    Map<String, String> errors = editAccountService.edit(form, checkPassword, accountId);
        if(!errors.isEmpty()){
            model.addAllAttributes(errors);
            model.addAttribute("editPage", true);
            return "account/userAccount";
        }

    session.setAttribute("username", form.getUsername());

    editAccountService.fillCurrentValues(form, accountId);
    model.addAttribute("editSuccessMessage", "更新が完了しました。");
    model.addAttribute("editPage", false);
    return "account/userAccount";
    }
}