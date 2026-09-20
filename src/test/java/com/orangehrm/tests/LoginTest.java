package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.ConfigReader;

public class LoginTest extends BaseTest {

    @Test
    public void verifySuccessfulLogin() {

        LoginPage loginPage = new LoginPage(driver);

        DashboardPage dashboardPage =
                loginPage.login(
                        ConfigReader.get("username"),
                        ConfigReader.get("password")
                );

        Assert.assertTrue(
                dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after successful login"
        );
    }
}