package com.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HeaderPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By userDropdown =
            By.xpath("//span[contains(@class,'oxd-userdropdown-tab')]");

    private final By logoutLink =
            By.xpath("//a[normalize-space()='Logout']");

    public HeaderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public LoginPage logout() {

        wait.until(
                ExpectedConditions.elementToBeClickable(userDropdown)
        ).click();

        wait.until(
                ExpectedConditions.elementToBeClickable(logoutLink)
        ).click();

        return new LoginPage(driver);
    }
}