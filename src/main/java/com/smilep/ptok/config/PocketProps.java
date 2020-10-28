package com.smilep.ptok.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pocket")
public class PocketProps {
    private String url;
    private String username;
    private String password;
}
