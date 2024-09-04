package com.hh.multiboarduserbackend.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties(prefix = "jwt")
@ConfigurationPropertiesBinding
public record JwtProperties(
          String secret
        , String issuer
        , long expiration
) {
}