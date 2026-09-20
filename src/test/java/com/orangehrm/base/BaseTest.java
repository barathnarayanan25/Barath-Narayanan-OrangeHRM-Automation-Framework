package com.orangehrm.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.orangehrm.driver.DriverFactory;
import com.orangehrm.utils.ConfigReader;

public class BaseTest {

	protected WebDriver driver;

    @BeforeMethod
    public void setUp() {

        String browser = ConfigReader.get("browser");
        String baseUrl = ConfigReader.get("baseUrl");

        driver = DriverFactory.createDriver(browser);

        driver.get(baseUrl);
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
    
    public WebDriver getDriver() {
        return driver;
    }
}