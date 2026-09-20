package com.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By usernameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.cssSelector("button[type='submit']");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions
    public void enterUsername(String username) {

        WebElement usernameElement =
                wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));

        usernameElement.clear();
        usernameElement.sendKeys(username);
    }

    public void enterPassword(String password) {

        WebElement passwordElement =
                wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));

        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    public DashboardPage clickLogin() {

        wait.until(ExpectedConditions.elementToBeClickable(loginButton))
                .click();

        return new DashboardPage(driver);
    }

    public DashboardPage login(String username, String password) {

        enterUsername(username);
        enterPassword(password);

        return clickLogin();
    }
    
    public boolean isLoginPageDisplayed() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameField)
        ).isDisplayed();
    }
}