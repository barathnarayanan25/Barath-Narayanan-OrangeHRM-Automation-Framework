package com.orangehrm.utils;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extentReports;

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getExtentReports() {

        if (extentReports == null) {

            String reportPath = System.getProperty("user.dir")
                    + File.separator + "reports"
                    + File.separator + "ExtentReport.html";

            ExtentSparkReporter sparkReporter =
                    new ExtentSparkReporter(reportPath);

            sparkReporter.config().setDocumentTitle("OrangeHRM Automation Report");
            sparkReporter.config().setReportName("OrangeHRM QA Automation");
            sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

            extentReports = new ExtentReports();

            extentReports.attachReporter(sparkReporter);

            extentReports.setSystemInfo("Project", "OrangeHRM QA Automation");
            extentReports.setSystemInfo("Automation", "Selenium + Java + TestNG");
            extentReports.setSystemInfo("API", "REST Assured");
            extentReports.setSystemInfo("Browser", "Chrome");
            extentReports.setSystemInfo("Environment", "OrangeHRM Demo");
        }

        return extentReports;
    }
}