package com.mrkotuk.PersoNet.domain.dto;

import lombok.Getter;

@Getter
public class ForgotPasswordDTO {
    private String email;
    private String verifyEmailToken;
    private String newPassword;
}
