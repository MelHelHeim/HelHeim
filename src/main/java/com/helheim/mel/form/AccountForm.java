package com.helheim.mel.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountForm {
    private String id;
    private String username;
    private String password;
    private String email;
    private boolean verified;
    private String address;
    private String phone;

    private String usernameOrEmail;
}
