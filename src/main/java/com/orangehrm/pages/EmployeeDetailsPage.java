package com.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmployeeDetailsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstNameField =
            By.cssSelector("input[name='firstName']");

    private final By lastNameField =
            By.cssSelector("input[name='lastName']");

    private final By employeeIdField =
            By.xpath("//label[normalize-space()='Employee Id']/following::input[1]");

    private final By personalDetailsHeading =
            By.xpath("//h6[normalize-space()='Personal Details']");
    
    private final By successMessage =
            By.xpath("//*[normalize-space()='Successfully Saved']");
    
    private final By jobTab =
            By.xpath("//a[normalize-space()='Job']");

    public EmployeeDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isEmployeeSavedSuccessfully() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        successMessage
                )
        ).isDisplayed();
    }
    
    public String getFirstName() {

        wait.until(driver -> {
            String value = driver.findElement(firstNameField)
                    .getAttribute("value");

            return value != null && !value.trim().isEmpty();
        });

        return driver.findElement(firstNameField)
                .getAttribute("value");
    }

    public String getLastName() {

        wait.until(driver -> {
            String value = driver.findElement(lastNameField)
                    .getAttribute("value");

            return value != null && !value.trim().isEmpty();
        });

        return driver.findElement(lastNameField)
                .getAttribute("value");
    }

    public String getEmployeeId() {

        wait.until(driver -> {
            String value = driver.findElement(employeeIdField)
                    .getAttribute("value");

            return value != null && !value.trim().isEmpty();
        });

        return driver.findElement(employeeIdField)
                .getAttribute("value");
    }

    public boolean isPersonalDetailsDisplayed() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        personalDetailsHeading
                )
        ).isDisplayed();
    }
    
    public EmployeeJobPage clickJob() {

        wait.until(
                ExpectedConditions.elementToBeClickable(jobTab)
        ).click();

        // Wait for Job page content to become available
        By jobTitleLabel =
                By.xpath("//label[normalize-space()='Job Title']");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(jobTitleLabel)
        );

        return new EmployeeJobPage(driver);
    }
}