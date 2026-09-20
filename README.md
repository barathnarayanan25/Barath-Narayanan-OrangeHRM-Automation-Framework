# OrangeHRM QA Automation

## Project Overview

This project is an end-to-end QA automation framework developed for the OrangeHRM application.

The framework covers:

- UI automation using Selenium WebDriver
- Page Object Model (POM)
- TestNG test execution
- Data-driven employee creation using JSON
- OAuth 2.0 with PKCE authentication
- REST API validation using REST Assured
- UI and API data comparison
- Employee lifecycle validation
- Extent HTML reporting
- API performance testing using Apache JMeter

---

## Application Under Test

**Application:** OrangeHRM Demo

**URL:** https://opensource-demo.orangehrmlive.com/

The automation validates the employee lifecycle from creation through deletion, including UI, API, authentication, reporting, and performance testing.

---

## Technology Stack

| Technology         | Purpose                         |
|--------------------|---------------------------------|
| Java 17            | Programming language            |
| Selenium WebDriver | UI automation                   |
| TestNG             | Test execution and assertions   |
| Maven              | Build and dependency management |
| REST Assured       | API automation                  |
| OAuth 2.0 + PKCE   | API authentication              |
| Jackson            | JSON test-data handling         |
| Extent Reports     | HTML reporting                  |
| Apache JMeter      | Performance testing             |
| Git                | Version control                 |

---

## Framework Structure

```text
orangehrm-qa-automation
│
├── src
│   ├── main
│   │   └── java
│   │       └── com.orangehrm
│   │           ├── api
│   │           ├── driver
│   │           ├── models
│   │           ├── pages
│   │           └── utils
│   │
│   └── test
│       ├── java
│       │   └── com.orangehrm
│       │       ├── base
│       │       ├── tests
│       │       └── utils
│       │
│       └── resources
│           └── testdata
│
├── performance
│   ├── OrangeHRM API Load Test.jmx
│   └── results
│       └── OrangeHRM_API_Performance.jtl
│
├── reports
│   └── ExtentReport.html
│
├── pom.xml
└── testng.xml
```

---

## Design Approach

The framework follows the Page Object Model (POM) design pattern.

Each application page has a dedicated page class containing:

- Web element locators
- Page-specific actions
- Synchronization logic
- Navigation methods

The test classes contain the business flow and assertions, while reusable framework functionality is maintained separately.

---

## Employee Lifecycle Test

The main end-to-end test covers the following flow:

```text
Login
  ↓
Dashboard Verification
  ↓
Navigate to PIM
  ↓
Add Employee
  ↓
Upload Profile Picture
  ↓
Create Employee
  ↓
Capture Generated Employee ID
  ↓
Search Employee
  ↓
Open Employee
  ↓
Update Job Title
  ↓
Update Employment Status
  ↓
Validate Updated Details
  ↓
API Validation
  ↓
UI/API Data Comparison
  ↓
Delete Employee Through UI
  ↓
API Deletion Verification
  ↓
Logout
  ↓
Session Invalidation Verification
```

---

## Data-Driven Employee Creation

Employee test data is maintained separately in:

```text
src/test/resources/testdata/employee.json
```

Example data includes:

- First Name
- Last Name
- Job Title
- Employment Status
- Profile Picture

Jackson is used to deserialize the JSON test data into the `Employee` model.

---

## API Validation

The framework uses REST Assured for API validation.

The API validation flow is:

1. Authenticate using OAuth 2.0 Authorization Code flow with PKCE.
2. Obtain an access token.
3. Search the employee directory using the generated Employee ID.
4. Retrieve the internal employee number.
5. Retrieve the employee using the employee API.
6. Validate the employee details.
7. Compare UI and API employee information.
8. After UI deletion, verify that the employee can no longer be retrieved through the API.

### API Validation Results

The successful execution demonstrated:

```text
OAuth token response status: 200
Directory API Status: 200
Employee API Status: 200
UI and API employee details matched successfully.
API deletion verification passed.
```

---

## OAuth 2.0 with PKCE

OAuth authentication is implemented using the Authorization Code flow with PKCE.

The framework:

1. Generates a code verifier.
2. Generates the S256 code challenge.
3. Generates a state value.
4. Opens the OrangeHRM authorization URL.
5. Approves the OAuth consent.
6. Receives the authorization code through a local callback server.
7. Exchanges the authorization code for an access token.
8. Uses the access token for authenticated API requests.

OAuth credentials and access tokens are not intended to be committed to the repository.

---

## Reporting

Extent Reports are integrated using a TestNG listener.

The report provides:

- Test execution status
- Step-level execution details
- Pass/fail information
- Failure screenshots
- Environment information

Report location:

```text
reports/ExtentReport.html
```

---

## Performance Testing

Apache JMeter is used to perform API performance testing against the OrangeHRM Employee Directory API.

### Test Configuration

```text
Virtual Users:     10
Ramp-up Period:    10 seconds
Iterations/User:   5
Total Requests:    50
```

A response assertion validates that the API returns HTTP `200`.

### Final Performance Results

| Metric | Result |
|---|---:|
| Samples | 50 |
| Average Response Time | 545 ms |
| Median | 484 ms |
| 90th Percentile | 801 ms |
| 95th Percentile | 819 ms |
| 99th Percentile | 841 ms |
| Minimum | 464 ms |
| Maximum | 841 ms |
| Error Rate | 0.00% |
| Throughput | 4.3 requests/sec |

Performance result file:

```text
performance/results/OrangeHRM_API_Performance.jtl
```

JMeter test plan:

```text
performance/OrangeHRM API Load Test.jmx
```

---

## How to Run the Tests

### Prerequisites

Install:

- Java 17 or later
- Maven
- Google Chrome
- Apache JMeter 5.6.3 for performance testing

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

### Run the TestNG Suite

From the project root:

```bash
mvn test
```

### Clean and Run

```bash
mvn clean test
```

---

## Test Execution Result

The final functional automation execution completed successfully with:

```text
Tests run: 1
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

The end-to-end test successfully validated:

- Employee creation
- Employee search
- Employee update
- OAuth authentication
- API validation
- UI/API data comparison
- Employee deletion
- API deletion verification
- Logout
- Session invalidation

---

## AI Usage

AI tools were used as an engineering assistance tool during the development of this assignment.

AI assistance was used for:

- Framework design suggestions
- Page Object Model structuring
- Selenium locator troubleshooting
- Test automation debugging
- API/OAuth implementation guidance
- JMeter test-plan setup
- Troubleshooting and code refinement
- Documentation assistance

All implementation was reviewed, executed, and validated locally against the OrangeHRM application.

---

## Video Demonstration

A video demonstration of the automation framework covers:

- Project structure
- Test execution
- Employee lifecycle
- API and OAuth validation
- Employee deletion
- Logout and session invalidation
- Extent Report
- JMeter performance results

Video:

```text
Barath_Narayanan_OrangeHRM_QA_Automation_Demo.mp4
```

---

## Author

**Barath Narayanan**

QA Automation / SDET

Technologies demonstrated in this project:

`Java` `Selenium` `TestNG` `REST Assured` `OAuth 2.0` `PKCE` `Maven` `JMeter` `Extent Reports`
