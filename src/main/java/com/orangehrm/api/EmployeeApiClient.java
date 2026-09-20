package com.orangehrm.api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class EmployeeApiClient {

    private final String baseUrl;
    private final String accessToken;

    public EmployeeApiClient(String baseUrl, String accessToken) {
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
    }

    public Response searchEmployeeDirectory(String employeeId) {

        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .queryParam("nameOrId", employeeId)
        .when()
                .get("/directory/employees");
    }

    public Response getEmployee(String empNumber) {

        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
        .when()
                .get("/pim/employees/" + empNumber);
    }

    public Response deleteEmployee(String empNumber) {

        return given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body("{\"ids\":[" + empNumber + "]}")
        .when()
                .delete("/pim/employees");
    }
}