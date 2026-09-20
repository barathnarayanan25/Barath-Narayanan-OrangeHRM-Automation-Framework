package com.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PIMPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pimMenu = By.xpath("//li//span[normalize-space()='PIM']");
    private final By addEmployeeMenu =
            By.xpath("//a[normalize-space()='Add Employee']");

    public PIMPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickPIM() {

        wait.until(
                ExpectedConditions.elementToBeClickable(pimMenu)
        ).click();
    }

    public AddEmployeePage clickAddEmployee() {

        wait.until(
                ExpectedConditions.elementToBeClickable(addEmployeeMenu)
        ).click();

        return new AddEmployeePage(driver);
    }

    public AddEmployeePage navigateToAddEmployee() {

        clickPIM();

        return clickAddEmployee();
    }
}