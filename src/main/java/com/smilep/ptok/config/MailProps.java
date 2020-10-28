package com.smilep.ptok.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mail")
public class MailProps {
    private String host;
    private String port;
    private boolean sslenable;
    private String from;
    private String password;
    private boolean auth;
    private String to;
}
