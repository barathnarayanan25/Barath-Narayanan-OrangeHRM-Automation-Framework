package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseTest;
import com.orangehrm.models.Employee;
import com.orangehrm.pages.AddEmployeePage;
import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.EmployeeDetailsPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.JsonDataReader;
import com.orangehrm.pages.EmployeeListPage;
import com.orangehrm.pages.HeaderPage;
import com.orangehrm.pages.EmployeeJobPage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.orangehrm.api.EmployeeApiClient;
import com.orangehrm.api.OAuthTokenProvider;
import io.restassured.response.Response;
import com.aventstack.extentreports.Status;
import com.orangehrm.utils.TestListener;

public class EmployeeLifecycleTest extends BaseTest {

    @Test
    public void verifyEmployeeCreation() {

        // Load employee test data from JSON
        Employee employee = JsonDataReader.getEmployeeData();

        // Login
        LoginPage loginPage = new LoginPage(driver);

        DashboardPage dashboardPage =
                loginPage.login(
                        ConfigReader.get("username"),
                        ConfigReader.get("password")
                );

        Assert.assertTrue(
                dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after login"
        );

        // Navigate to PIM -> Add Employee
        PIMPage pimPage = new PIMPage(driver);

        AddEmployeePage addEmployeePage =
                pimPage.navigateToAddEmployee();

        // Capture auto-generated Employee ID
        String generatedEmployeeId =
                addEmployeePage.getEmployeeId();

        employee.setEmployeeId(generatedEmployeeId);

        System.out.println(
                "Generated Employee ID: " + generatedEmployeeId
        );
        
        TestListener.getTest().log(
        	    Status.PASS,
        	    "Employee ID generated: " + employee.getEmployeeId()
        	);

        Assert.assertFalse(
                employee.getEmployeeId().isEmpty(),
                "Employee ID should be generated automatically"
        );

     // Enter employee details
        addEmployeePage.enterFirstName(
                employee.getFirstName()
        );

        addEmployeePage.enterLastName(
                employee.getLastName()
        );

        // Upload profile picture
        Path profilePicturePath =
                Paths.get(employee.getProfilePicture())
                      .toAbsolutePath()
                      .normalize();
        
        if (!Files.exists(profilePicturePath)) {
            throw new RuntimeException(
                    "Profile picture not found: " + profilePicturePath
            );
        }

        addEmployeePage.uploadProfilePicture(
                profilePicturePath.toString()
        );

        System.out.println(
                "Profile picture uploaded: "
                + profilePicturePath
        );

        // Disable Create Login Details
        addEmployeePage.disableCreateLoginDetails();
        // Save employee
        EmployeeDetailsPage employeeDetailsPage =
                addEmployeePage.clickSave();
        
        // Verify employee creation success message
        Assert.assertTrue(
                employeeDetailsPage.isEmployeeSavedSuccessfully(),
                "Successfully Saved message should be displayed after employee creation"
        );

        System.out.println(
                "Employee creation success message: Successfully Saved"
        );
        
        TestListener.getTest().log(
        	    Status.PASS,
        	    "Employee created successfully - Employee ID: "
        	    + employee.getEmployeeId()
        	);

        // Verify Personal Details page
        Assert.assertTrue(
                employeeDetailsPage.isPersonalDetailsDisplayed(),
                "Personal Details page should be displayed after employee creation"
        );

        // Retrieve employee details from UI
        String receivedFirstName =
                employeeDetailsPage.getFirstName();

        String receivedLastName =
                employeeDetailsPage.getLastName();

        String receivedEmployeeId =
                employeeDetailsPage.getEmployeeId();

        // Print received employee details
        System.out.println("========================================");
        System.out.println("Employee Details Received from UI");
        System.out.println("First Name  : " + receivedFirstName);
        System.out.println("Last Name   : " + receivedLastName);
        System.out.println("Employee ID : " + receivedEmployeeId);
        System.out.println("========================================");

        // Validate First Name
        Assert.assertEquals(
                receivedFirstName,
                employee.getFirstName(),
                "First name should match the entered employee data"
        );

        // Validate Last Name
        Assert.assertEquals(
                receivedLastName,
                employee.getLastName(),
                "Last name should match the entered employee data"
        );

        // Validate Employee ID
        Assert.assertEquals(
                receivedEmployeeId,
                employee.getEmployeeId(),
                "Employee ID should match the generated Employee ID"
        );

        pimPage.clickPIM();

        EmployeeListPage employeeListPage =
                new EmployeeListPage(driver);

        employeeListPage.clickEmployeeList();

        // Verify Employee List page
        Assert.assertTrue(
                employeeListPage.isEmployeeListDisplayed(),
                "Employee List page should be displayed"
        );

        // Search employee using generated Employee ID
        employeeListPage.searchByEmployeeId(
                employee.getEmployeeId()
        );

        // Verify employee appears in search results
        Assert.assertTrue(
                employeeListPage.isEmployeePresent(
                        employee.getEmployeeId()
                ),
                "Employee should be present in search results"
        );

        System.out.println(
                "Employee successfully found using Employee ID: "
                + employee.getEmployeeId()
        );
        
        TestListener.getTest().log(
        	    Status.PASS,
        	    "Employee successfully found using Employee ID: "
        	    + employee.getEmployeeId()
        	);
        
     // Click Edit icon for the employee
        employeeDetailsPage =
                employeeListPage.clickEditEmployee(
                        employee.getEmployeeId()
                );

        System.out.println(
                "Successfully opened employee in Edit mode: "
                + employee.getEmployeeId()
        );

        // Navigate to Job tab
        EmployeeJobPage employeeJobPage =
                employeeDetailsPage.clickJob();

        System.out.println(
                "Successfully navigated to Employee Job Details"
        );
        
        // Update Job Title
        employeeJobPage.selectJobTitle(
                employee.getJobTitle()
        );

        System.out.println(
                "Job Title updated to: "
                + employee.getJobTitle()
        );
        
        TestListener.getTest().log(
        	    Status.PASS,
        	    "Job Title updated successfully: QA Engineer"
        	);

        // Update Employment Status
        employeeJobPage.selectEmploymentStatus(
                employee.getEmploymentStatus()
        );

        System.out.println(
                "Employment Status updated to: "
                + employee.getEmploymentStatus()
        );
        
        TestListener.getTest().log(
        	    Status.PASS,
        	    "Employment Status updated successfully: Full-Time Permanent"
        	);
        
        // Save Job Details
        employeeJobPage.clickSave();
        
        // Verify success message
        Assert.assertTrue(
                employeeJobPage.isSuccessfullyUpdated(),
                "Successfully Saved message should be displayed after updating Job Details"
        );

        System.out.println(
        		"Job Details update success message: Successfully Updated"
        );
        
        // Verify Job Title
        String updatedJobTitle =
                employeeJobPage.getJobTitle();

        Assert.assertEquals(
                updatedJobTitle,
                employee.getJobTitle(),
                "Job Title should match the updated employee data"
        );

        // Verify Employment Status
        String updatedEmploymentStatus =
                employeeJobPage.getEmploymentStatus();

        Assert.assertEquals(
                updatedEmploymentStatus,
                employee.getEmploymentStatus(),
                "Employment Status should match the updated employee data"
        );

        System.out.println("========================================");
        System.out.println("Updated Job Details");
        System.out.println("Job Title         : " + updatedJobTitle);
        System.out.println("Employment Status : " + updatedEmploymentStatus);
        System.out.println("========================================");
        
     // ================= API VALIDATION =================

        System.out.println("Starting API validation for Employee ID: "
                + employee.getEmployeeId());

        // Generate OAuth access token
        String accessToken = OAuthTokenProvider.getAccessToken(driver);

        Assert.assertNotNull(
                accessToken,
                "OAuth access token should not be null"
        );

        Assert.assertFalse(
                accessToken.isBlank(),
                "OAuth access token should not be empty"
        );

        // Create API client
        EmployeeApiClient apiClient =
                new EmployeeApiClient(
                        ConfigReader.get("apiBaseUrl"),
                        accessToken
                );

        // Search employee using the Employee ID generated by this test
        Response directoryResponse =
                apiClient.searchEmployeeDirectory(
                        employee.getEmployeeId()
                );

        System.out.println("Directory API Status: "
                + directoryResponse.statusCode());

        Assert.assertEquals(
                directoryResponse.statusCode(),
                200,
                "Directory API should return HTTP 200"
        );

        int employeeCount =
                directoryResponse
                        .jsonPath()
                        .getInt("meta.total");

        Assert.assertTrue(
                employeeCount > 0,
                "Employee with ID "
                        + employee.getEmployeeId()
                        + " was not found through Directory API"
        );

        // Get internal OrangeHRM employee number
        Integer empNumber =
                directoryResponse
                        .jsonPath()
                        .getInt("data[0].empNumber");

        Assert.assertNotNull(
                empNumber,
                "Internal employee number should be returned"
        );

        System.out.println(
                "Internal Employee Number: " + empNumber
        );

        // Get employee details through API
        Response employeeResponse =
                apiClient.getEmployee(
                        String.valueOf(empNumber)
                );

        System.out.println(
                "Employee API Status: "
                        + employeeResponse.statusCode()
        );

        Assert.assertEquals(
                employeeResponse.statusCode(),
                200,
                "Employee API should return HTTP 200"
        );

        // API values
        String apiEmployeeId =
                employeeResponse
                        .jsonPath()
                        .getString("data.employeeId");

        String apiFirstName =
                employeeResponse
                        .jsonPath()
                        .getString("data.firstName");

        String apiLastName =
                employeeResponse
                        .jsonPath()
                        .getString("data.lastName");

        // Print API values
        System.out.println("API Employee ID: " + apiEmployeeId);
        System.out.println("API First Name: " + apiFirstName);
        System.out.println("API Last Name: " + apiLastName);

        // Cross-check UI and API
        Assert.assertEquals(
                apiEmployeeId,
                employee.getEmployeeId(),
                "UI and API Employee ID should match"
        );

        Assert.assertEquals(
                apiFirstName,
                employee.getFirstName(),
                "UI and API First Name should match"
        );

        Assert.assertEquals(
                apiLastName,
                employee.getLastName(),
                "UI and API Last Name should match"
        );

        System.out.println(
                "UI and API employee details matched successfully."
        );   
        
        TestListener.getTest().log(
        	    Status.PASS,
        	    "UI and API employee details matched successfully"
        	);
        
     // ================= UI DELETE =================

        System.out.println(
                "Starting employee deletion through UI..."
        );
        
     // Return browser to OrangeHRM
        driver.get(ConfigReader.get("baseUrl"));

     if (!dashboardPage.isDashboardDisplayed()) {

         dashboardPage =
                 loginPage.login(
                         ConfigReader.get("username"),
                         ConfigReader.get("password")
                 );

         Assert.assertTrue(
                 dashboardPage.isDashboardDisplayed(),
                 "Dashboard should be displayed before deletion"
         );
     }
     
     	pimPage.clickPIM();

         employeeListPage =
                new EmployeeListPage(driver);

         employeeListPage.searchByEmployeeId(
                employee.getEmployeeId()
        );

        Assert.assertTrue(
                employeeListPage.isEmployeePresent(
                        employee.getEmployeeId()
                ),
                "Employee should exist before deletion"
        );

        employeeListPage.deleteEmployee(
                employee.getEmployeeId()
        );

        System.out.println(
                "Employee deleted successfully through UI: "
                        + employee.getEmployeeId()
        );
        
        TestListener.getTest().log(
        	    Status.PASS,
        	    "Employee deleted successfully through UI: "
        	    + employee.getEmployeeId()
        	);
        
     // ================= API DELETION VERIFICATION =================

        Response deletedEmployeeResponse =
                apiClient.getEmployee(
                        String.valueOf(empNumber)
                );

        System.out.println(
                "API deletion verification status: "
                        + deletedEmployeeResponse.statusCode()
        );

        Assert.assertNotEquals(
                deletedEmployeeResponse.statusCode(),
                200,
                "Deleted employee should not be retrievable through API"
        );

        System.out.println(
                "API deletion verification passed."
        );
        
        TestListener.getTest().log(
        	    Status.PASS,
        	    "API deletion verification passed. HTTP status: "
        	    + deletedEmployeeResponse.statusCode()
        	);
        
     // ================= LOGOUT =================

        System.out.println("Starting logout...");

        HeaderPage headerPage = new HeaderPage(driver);
        loginPage = headerPage.logout();

        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "Login page should be displayed after logout"
        );

        System.out.println("Logout successful.");

        TestListener.getTest().log(
                Status.PASS,
                "User logged out successfully"
        );
        
     // ================= SESSION INVALIDATION =================

        driver.get(
                ConfigReader.get("baseUrl")
                + "web/index.php/dashboard/index"
        );

        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "Logged-out user should not access Dashboard directly"
        );

        System.out.println("Session invalidation verified.");

        TestListener.getTest().log(
                Status.PASS,
                "Session invalidation verified - Dashboard redirected to Login page"
        );
    }
    
}