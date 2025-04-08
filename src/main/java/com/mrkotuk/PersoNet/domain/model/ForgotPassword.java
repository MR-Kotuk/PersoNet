package com.mrkotuk.PersoNet.domain.model;

import lombok.Getter;

@Getter
public class ForgotPassword {
    private String email;
    private String verifyEmailToken;
    private String newPassword;
}
