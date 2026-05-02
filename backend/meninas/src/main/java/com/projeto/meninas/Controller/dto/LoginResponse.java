package com.projeto.meninas.Controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private Integer expiresIn;

    public String getAccesstoken() {
        return accessToken;
    }
}
