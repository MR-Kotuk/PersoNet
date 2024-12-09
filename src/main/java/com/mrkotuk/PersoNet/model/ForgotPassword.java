package com.mrkotuk.PersoNet.model;

import lombok.Getter;

@Getter
public class ForgotPassword {
    private String email;
    private String verifyEmailToken;
    private String newPassword;
}