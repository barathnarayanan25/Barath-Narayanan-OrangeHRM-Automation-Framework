package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.api.EmployeeApiClient;
import com.orangehrm.api.OAuthTokenProvider;
import com.orangehrm.base.BaseTest;
import com.orangehrm.utils.ConfigReader;

import io.restassured.response.Response;

public class EmployeeApiTest extends BaseTest {

    @Test
    public void verifyEmployeeCanBeRetrievedThroughApi() {

        // Login to OrangeHRM
        new com.orangehrm.pages.LoginPage(driver)
                .login(
                        ConfigReader.get("username"),
                        ConfigReader.get("password")
                );

        // Generate OAuth access token
        String accessToken =
                OAuthTokenProvider.getAccessToken(driver);

        Assert.assertNotNull(
                accessToken,
                "Access token should not be null"
        );

        // Use the Employee ID created during our UI test
        String employeeId = "0411";

        EmployeeApiClient apiClient =
                new EmployeeApiClient(
                        ConfigReader.get("apiBaseUrl"),
                        accessToken
                );

        // Search employee through Directory API
        Response directoryResponse =
                apiClient.searchEmployeeDirectory(employeeId);

        System.out.println(
                "Directory API Status: "
                        + directoryResponse.statusCode()
        );

        System.out.println(
                "Directory API Response:"
        );

        System.out.println(
                directoryResponse.asPrettyString()
        );

        Assert.assertEquals(
                directoryResponse.statusCode(),
                200,
                "Directory API should return HTTP 200"
        );

        // Extract internal OrangeHRM employee number
        int employeeCount =
                directoryResponse
                        .jsonPath()
                        .getInt("meta.total");

        Assert.assertTrue(
                employeeCount > 0,
                "Employee with Employee ID " + employeeId
                        + " was not found in the Directory API"
        );

        Integer empNumber =
                directoryResponse
                        .jsonPath()
                        .getInt("data[0].empNumber");

        Assert.assertNotNull(
                empNumber,
                "Internal employee number should be returned"
        );
        System.out.println(
                "Internal Employee Number: "
                        + empNumber
        );

        // Retrieve employee details
        Response employeeResponse =
                apiClient.getEmployee(
                        String.valueOf(empNumber)
                );

        System.out.println(
                "Employee API Status: "
                        + employeeResponse.statusCode()
        );

        System.out.println(
                "Employee API Response:"
        );

        System.out.println(
                employeeResponse.asPrettyString()
        );

        Assert.assertEquals(
                employeeResponse.statusCode(),
                200,
                "Employee API should return HTTP 200"
        );

        // Validate Employee ID
        String apiEmployeeId =
                employeeResponse
                        .jsonPath()
                        .getString("data.employeeId");

        Assert.assertEquals(
                apiEmployeeId,
                employeeId,
                "API Employee ID should match UI Employee ID"
        );

        System.out.println(
                "API Employee ID: "
                        + apiEmployeeId
        );

        System.out.println(
                "Employee API validation passed."
        );
    }
}