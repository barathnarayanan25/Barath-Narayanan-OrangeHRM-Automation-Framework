package com.orangehrm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmployeeListPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By employeeListMenu =
            By.xpath("//a[normalize-space()='Employee List']");

    private final By employeeIdSearchField =
            By.xpath("//label[normalize-space()='Employee Id']/following::input[1]");

    private final By searchButton =
            By.xpath("//button[normalize-space()='Search']");

    private final By resetButton =
            By.xpath("//button[normalize-space()='Reset']");

    private final By employeeTable =
            By.xpath("//div[@role='table']");
    
    private final By deleteConfirmationButton =
            By.xpath("//button[normalize-space()='Yes, Delete']");

    private final By deleteDialog =
            By.xpath("//div[contains(@class,'orangehrm-modal-header')]");

    public EmployeeListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickEmployeeList() {

        wait.until(
                ExpectedConditions.elementToBeClickable(employeeListMenu)
        ).click();
    }

    public void searchByEmployeeId(String employeeId) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        employeeIdSearchField
                )
        ).sendKeys(employeeId);

        wait.until(
                ExpectedConditions.elementToBeClickable(searchButton)
        ).click();
    }

    public boolean isEmployeePresent(String employeeId) {

        By employeeCell = By.xpath(
            "//div[@role='table']//div[@role='row']//div[contains(@class,'oxd-table-cell')][normalize-space()='"
            + employeeId + "']"
        );

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return !driver.findElements(employeeCell).isEmpty();

            } catch (StaleElementReferenceException e) {
                System.out.println(
                    "Employee table refreshed while checking employee. Retrying... attempt "
                    + attempt
                );
            }
        }

        return false;
    }

    public boolean isEmployeeListDisplayed() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        employeeTable
                )
        ).isDisplayed();
    }

    public void resetSearch() {

        wait.until(
                ExpectedConditions.elementToBeClickable(resetButton)
        ).click();
    }
    
    public EmployeeDetailsPage clickEditEmployee(String employeeId) {

        By employeeRow = By.xpath(
                "//div[@role='row'][.//div[normalize-space()='"
                        + employeeId + "']]"
        );

        By editButton = By.xpath(
                "//div[@role='row'][.//div[normalize-space()='"
                        + employeeId + "']]"
                        + "//button[.//i[contains(@class,'bi-pencil-fill')]]"
        );

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(employeeRow)
        );

        for (int attempt = 1; attempt <= 3; attempt++) {

            try {

                // Always locate a fresh element
                WebElement button =
                        wait.until(
                                ExpectedConditions.presenceOfElementLocated(
                                        editButton
                                )
                        );

                // Scroll the fresh element into view
                ((JavascriptExecutor) driver)
                        .executeScript(
                                "arguments[0].scrollIntoView({block:'center'});",
                                button
                        );

                // Wait until the fresh element is clickable
                wait.until(
                        ExpectedConditions.elementToBeClickable(editButton)
                );

                // Re-locate immediately before clicking
                driver.findElement(editButton).click();

                return new EmployeeDetailsPage(driver);

            } catch (StaleElementReferenceException e) {

                System.out.println(
                        "Edit button became stale. Retrying... attempt "
                                + attempt
                );
            }
        }

        throw new RuntimeException(
                "Unable to click Edit button for Employee ID: "
                        + employeeId
        );
    }
    
    public void deleteEmployee(String employeeId) {

        By employeeRow = By.xpath(
            "//div[@role='row'][.//div[normalize-space()='" 
            + employeeId + "']]"
        );

        By deleteButton = By.xpath(
            "//div[@role='row'][.//div[normalize-space()='" 
            + employeeId + "']]"
            + "//button[.//i[contains(@class,'bi-trash')]]"
        );

        // Wait for the employee row to appear
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(employeeRow)
        );

        // OrangeHRM can refresh the table, so locate the button again
        // immediately before clicking it.
        for (int attempt = 1; attempt <= 3; attempt++) {

            try {

                WebElement button = wait.until(
                    ExpectedConditions.presenceOfElementLocated(deleteButton)
                );

                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    button
                );

                wait.until(
                    ExpectedConditions.elementToBeClickable(deleteButton)
                );

                // Fresh lookup immediately before click
                driver.findElement(deleteButton).click();

                break;

            } catch (StaleElementReferenceException e) {

                System.out.println(
                    "Delete button became stale. Retrying... attempt "
                    + attempt
                );

                if (attempt == 3) {
                    throw new RuntimeException(
                        "Unable to click Delete button for Employee ID: "
                        + employeeId,
                        e
                    );
                }
            }
        }

        // Wait for confirmation dialog
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(deleteDialog)
        );

        // Confirm deletion
        wait.until(
            ExpectedConditions.elementToBeClickable(deleteConfirmationButton)
        ).click();

        // Wait until the employee row disappears
        wait.until(
            ExpectedConditions.invisibilityOfElementLocated(employeeRow)
        );
    }
    
    public boolean isEmployeePresentAfterDeletion(String employeeId) {

        By employeeRow = By.xpath(
                "//div[@role='row'][.//div[normalize-space()='" + employeeId + "']]"
        );

        return !driver.findElements(employeeRow).isEmpty();
    }
  
}