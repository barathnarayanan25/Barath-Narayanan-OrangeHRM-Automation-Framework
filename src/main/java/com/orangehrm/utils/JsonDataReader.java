package com.orangehrm.utils;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangehrm.models.Employee;

public class JsonDataReader {

    private JsonDataReader() {
        // Prevent object creation
    }

    public static Employee getEmployeeData() {

        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream =
                     JsonDataReader.class
                             .getClassLoader()
                             .getResourceAsStream(
                                     "testdata/employee.json"
                             )) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "employee.json not found in test resources"
                );
            }

            return objectMapper.readValue(
                    inputStream,
                    Employee.class
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to read employee.json",
                    e
            );
        }
    }
}