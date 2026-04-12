package com.projeto.meninas.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class TokenConfig {

    @Value("${jwt.issuer:meninas-api}")
    private String issuer;

    @Value("${jwt.expires-in:3600}")
    private long expiresIn;

    public String getIssuer() {
        return issuer;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
