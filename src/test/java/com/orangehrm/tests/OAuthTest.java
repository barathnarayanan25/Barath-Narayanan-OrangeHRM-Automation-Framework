package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.api.OAuthTokenProvider;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.utils.ConfigReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class OAuthTest extends BaseTest {

    @Test
    public void verifyOAuthAuthentication() {

        LoginPage loginPage =
                new LoginPage(driver);

        DashboardPage dashboardPage =
                loginPage.login(
                        ConfigReader.get("username"),
                        ConfigReader.get("password")
                );

        Assert.assertTrue(
                dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after login"
        );

        String accessToken =
                OAuthTokenProvider.getAccessToken(driver);

        Path tokenFile = Path.of(
                System.getProperty("user.dir"),
                "performance",
                "oauth-token.txt"
        );

        try {
            Files.writeString(tokenFile, accessToken);
            System.out.println("OAuth access token saved for JMeter.");
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to save OAuth access token for JMeter.",
                    e
            );
        }

        Assert.assertNotNull(
                accessToken,
                "Access token should not be null"
        );

        Assert.assertFalse(
                accessToken.isBlank(),
                "Access token should not be empty"
        );

        System.out.println(
                "OAuth authentication test passed."
        );
    }
}