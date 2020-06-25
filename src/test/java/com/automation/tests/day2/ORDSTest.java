package com.automation.tests.day2;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class ORDSTest {
    String BASE_URL = "http://54.145.167.19:1000/ords/hr/";

    @Test
    @DisplayName("Get list of all employees")
    public void getAllEmployees(){
        Response response = given().
                                baseUri(BASE_URL).
                            when().
                                get("/employees").prettyPeek();



    }
}
