package com.smilep.ptok.factory;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SeleniumFactory {

    public ChromiumDriver getChromiumDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        ChromiumDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
    }

    static {
        // TODO - remove hardcoding
        // System.setProperty("webdriver.gecko.driver", "D:\\installed-usingpath\\geckodriver\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "D:\\installed-usingpath\\chromedriver\\chromedriver.exe");
        log.info("setting webdriver.chrome.driver with value {}", "D:\\installed-usingpath\\chromedriver\\chromedriver.exe");
    }

}
