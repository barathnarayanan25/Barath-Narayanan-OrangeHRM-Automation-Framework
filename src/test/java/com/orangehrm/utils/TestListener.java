package com.orangehrm.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.orangehrm.base.BaseTest;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {

    private static final ThreadLocal<ExtentTest> extentTest =
            new ThreadLocal<>();
    
    public static ExtentTest getTest() {
        return extentTest.get();
    }	

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest test =
                ExtentReportManager.getExtentReports()
                        .createTest(result.getMethod().getMethodName());

        extentTest.set(test);

        test.log(Status.INFO,
                "Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        extentTest.get().log(
                Status.PASS,
                "Test passed successfully.");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        extentTest.get().log(
                Status.FAIL,
                "Test failed: " + result.getThrowable());

        WebDriver driver = getDriver(result);

        if (driver != null) {

            String screenshotPath = captureScreenshot(driver);

            if (screenshotPath != null) {

                try {
                    extentTest.get().addScreenCaptureFromPath(
                            screenshotPath);
                } catch (Exception e) {
                    extentTest.get().log(
                            Status.WARNING,
                            "Unable to attach screenshot: "
                                    + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        extentTest.get().log(
                Status.SKIP,
                "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(
            org.testng.ITestContext testContext) {

        ExtentReportManager.getExtentReports()
                .flush();
    }

    private WebDriver getDriver(ITestResult result) {

        Object instance = result.getInstance();

        try {
            return ((BaseTest) instance).getDriver();

        } catch (Exception e) {
            return null;
        }
    }

    private String captureScreenshot(WebDriver driver) {

        try {

            Path screenshotDirectory =
                    Paths.get(System.getProperty("user.dir"),
                            "screenshots");

            Files.createDirectories(screenshotDirectory);

            String fileName =
                    "Failure_"
                    + System.currentTimeMillis()
                    + ".png";

            Path screenshotPath =
                    screenshotDirectory.resolve(fileName);

            File screenshotFile =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

            Files.copy(
                    screenshotFile.toPath(),
                    screenshotPath);

            return screenshotPath.toString();

        } catch (IOException e) {

            return null;
        }
    }
}