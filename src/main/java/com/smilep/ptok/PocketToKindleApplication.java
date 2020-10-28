package com.smilep.ptok;

import com.smilep.ptok.factory.SeleniumFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class PocketToKindleApplication {

    @Value("${driver.path}")
    private String driverPath;

    public static void main(String[] args) {
        SpringApplication.run(PocketToKindleApplication.class, args);
    }

    @Bean
    public SeleniumFactory seleniumFactory() {
        System.setProperty("webdriver.chrome.driver", driverPath);
        log.info("setting webdriver.chrome.driver with value {}", driverPath);
        return new SeleniumFactory();
    }

}
