package com.smilep.ptok.service;

import com.smilep.ptok.config.PocketProps;
import com.smilep.ptok.factory.SeleniumFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.openqa.selenium.*;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DocumentService {

    private SeleniumFactory seleniumFactory;

    private PocketProps pocketProps;

    public DocumentService(SeleniumFactory seleniumFactory, PocketProps pocketProps) {
        this.seleniumFactory = seleniumFactory;
        this.pocketProps = pocketProps;
    }

    public Path generate(int count) {
        ChromiumDriver driver = seleniumFactory.getChromiumDriver();
        try {
            return generate(driver, count);
        } catch (TimeoutException e) {
            log.error("", e);
        } finally {
            // clean up
            log.info("**************COMPLETED**************");
            cleanUp(driver);
        }
        return null;
    }

    private Path generate(ChromiumDriver driver, int count) {
        JavascriptExecutor javascriptExecutor = null;
        if (driver instanceof JavascriptExecutor) {
            javascriptExecutor = (JavascriptExecutor) driver;
        }
        driver.get(pocketProps.getUrl());

        log.info("**************navigating to log in page**************");
        WebElement loginElement = driver.findElement(By.linkText("Log In"));
        loginElement.click();

        log.info("**************logging in**************");
        WebElement username = driver.findElement(By.id("field-1"));
        WebElement password = driver.findElement(By.id("field-2"));
        WebElement submit = driver.findElement(By.className("login-btn-email"));
        username.sendKeys(pocketProps.getUsername());
        password.sendKeys(pocketProps.getPassword());
        submit.click();

        // clean output directories
        cleanOutput();

        log.info("**************fetching articles**************");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        List<WebElement> articles = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//article/*")));
        List<String> hrefs = articles.stream().map(article -> article.getAttribute("href")).filter(Objects::nonNull).collect(Collectors.toList());
        count = hrefs.size() < count ? hrefs.size() : count;
        List<Path> filePaths = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            log.info("**************fetching article no " + i + "**************");
            driver.navigate().to(hrefs.get(i));
            // wait for page to load
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("RIL_body")));
            if (null != javascriptExecutor) {
                log.info("**************removing the flying header**************");
                javascriptExecutor.executeScript("return document.getElementsByClassName(\"css-wm2fr9\")[0].remove();");
            }
            Path path = exportToPdf(driver, i);
            filePaths.add(path);
            log.info(driver.getTitle());
        }

        // combine PDFs
        log.info("**************combining PDFs**************");
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        try {
            for (Path path : filePaths) {
                pdfMergerUtility.addSource(path.toFile());
            }
        } catch (FileNotFoundException e) {
            log.error("", e);
        }
        Path finalDocPath = Path.of("output", LocalDateTime.now().toString().replaceAll(":", "-") + ".pdf");
        pdfMergerUtility.setDestinationFileName(finalDocPath.toString());
        try {
            pdfMergerUtility.mergeDocuments(null);
        } catch (IOException e) {
            log.error("", e);
        }
        return finalDocPath;
    }

    private static Path exportToPdf(ChromiumDriver driver, int docNum) {
        Path path = null;
        log.info("**************exporting to pdf**************");
        String command = "Page.printToPDF";
        Map<String, Object> params = new HashMap<>();
        params.put("landscape", false);
        params.put("printBackground", true);
        Map<String, Object> output = driver.executeCdpCommand(command, params);
        try {
            byte[] byteArray = Base64.getDecoder().decode((String) output.get("data"));
            Files.createDirectories(Path.of("output"));
            path = Path.of("output", "export" + docNum + ".pdf");
            Files.write(path, byteArray);
        } catch (IOException e) {
            log.error("", e);
            // throw new RuntimeException("Exception in exporting to pdf");
        }
        return path;
    }

    private static void cleanUp(WebDriver driver) {
        driver.close();
        driver.quit();
        //System.exit(0);
    }

    private static void cleanOutput() {
        try {
            Files.walk(Path.of("output")).sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
