package com.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddEmployeePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By firstNameField =
            By.name("firstName");

    private final By lastNameField =
            By.name("lastName");

    private final By employeeIdField =
            By.xpath("//label[normalize-space()='Employee Id']/following::input[1]");

    private final By profilePictureInput =
            By.cssSelector("input[type='file']");

    private final By createLoginDetailsCheckbox =
            By.xpath("//p[normalize-space()='Create Login Details']" +
                     "/following-sibling::div//input[@type='checkbox']");

    private final By createLoginDetailsLabel =
            By.xpath("//p[normalize-space()='Create Login Details']" +
                     "/following-sibling::div//label");

    private final By saveButton =
            By.xpath("//button[@type='submit' and normalize-space()='Save']");
    
    private final By formLoader =
            By.cssSelector(".oxd-form-loader");

    // Constructor
    public AddEmployeePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Actions

    public void enterFirstName(String firstName) {

        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(firstNameField)
        );

        element.clear();
        element.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {

        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(lastNameField)
        );

        element.clear();
        element.sendKeys(lastName);
    }

    public String getEmployeeId() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(employeeIdField)
        ).getAttribute("value");
    }

    public void uploadProfilePicture(String filePath) {

        wait.until(
                ExpectedConditions.presenceOfElementLocated(profilePictureInput)
        ).sendKeys(filePath);
    }

    public void disableCreateLoginDetails() {

        WebElement checkbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        createLoginDetailsCheckbox
                )
        );

        if (checkbox.isSelected()) {

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            createLoginDetailsLabel
                    )
            ).click();
        }

        wait.until(
                ExpectedConditions.elementSelectionStateToBe(
                        checkbox,
                        false
                )
        );
    }

    public EmployeeDetailsPage clickSave() {

        // Wait for any form loading overlay to disappear
        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        formLoader
                )
        );

        // Wait until Save button is clickable
        wait.until(
                ExpectedConditions.elementToBeClickable(saveButton)
        ).click();

        return new EmployeeDetailsPage(driver);
    }
}