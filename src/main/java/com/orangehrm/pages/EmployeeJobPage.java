package com.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmployeeJobPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By jobTitleDropdown =
            By.xpath(
                    "//label[normalize-space()='Job Title']" +
                    "/following::div[contains(@class,'oxd-select-text')][1]"
            );

    private final By employmentStatusDropdown =
            By.xpath(
                    "//label[normalize-space()='Employment Status']" +
                    "/following::div[contains(@class,'oxd-select-text')][1]"
            );

    private final By saveButton =
            By.xpath(
                    "//button[@type='submit' and normalize-space()='Save']"
            );

    private final By successMessage =
            By.xpath("//*[normalize-space()='Successfully Updated']");

    private final By formLoader =
            By.cssSelector(".oxd-form-loader");

    public EmployeeJobPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void selectJobTitle(String jobTitle) {

        // Wait for the Job form loader to disappear
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(formLoader)
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(jobTitleDropdown)
        ).click();

        By option = By.xpath(
                "//div[contains(@class,'oxd-select-option')][normalize-space()='"
                        + jobTitle + "']"
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(option)
        ).click();
    }

    public void selectEmploymentStatus(String employmentStatus) {

        // Wait for the Job form loader to disappear
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(formLoader)
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(employmentStatusDropdown)
        ).click();

        By option = By.xpath(
                "//div[contains(@class,'oxd-select-option')][normalize-space()='"
                        + employmentStatus + "']"
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(option)
        ).click();
    }

    public void clickSave() {

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        formLoader
                )
        );

        wait.until(
                ExpectedConditions.elementToBeClickable(saveButton)
        ).click();
    }

    public boolean isSuccessfullyUpdated() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        successMessage
                )
        ).isDisplayed();
    }

    public String getJobTitle() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        jobTitleDropdown
                )
        ).getText().trim();
    }

    public String getEmploymentStatus() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        employmentStatusDropdown
                )
        ).getText().trim();
    }
}