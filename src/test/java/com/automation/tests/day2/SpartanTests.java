package com.automation.tests.day2;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class SpartanTests {
    String BASE_URL = "http://54.145.167.19:8000/";

    @Test
    @DisplayName("Get list of all spartans")
    public void getAllSpartans(){
        // 401 - unauthorized, since we didn't provide credentials, request failed
        // how to provide credentials
        // There are different types of authentication: basic, oauth 1.0, oauth 2.0, api key, bearer token, etc...
        // spartan app requires basic authentication: username and password
        given().
                auth().basic("admin", "admin").
                baseUri(BASE_URL).
        when().
                get("/api/spartans").prettyPeek().
        then().statusCode(200);
    }

    // add new spartan

    @Test
    @DisplayName("Add new spartan")
    public void addSpartan(){
        // JSON supports different data types: string, integer, boolean
        String body = "{\"gender\": \"Male\", \"name\": \"Random User\", \"phone\": 9999999999}";
        // instead of string variable, we can external JSON file
        // use file class to read JSON and pass it into body
        // provide path to the JSON as a parameter
        File jsonFile = new File(System.getProperty("user.dir") + "/spartan.json");

        // to create new item, we perform POST request
        // contentType(ContentType.JSON) - to tell web service what kind of media type we send

        given().
                contentType(ContentType.JSON).
                auth().basic("admin", "admin").
                body(body).
                baseUri(BASE_URL).
        when().
                post("/api/spartans").prettyPeek().
        then().statusCode(201);
    }

    @Test
    @DisplayName("Delete some spartan")
    public void deleteSpartanTest(){
       // {id} - path parameter
        // you can not delete smth twice
        // we use delete() method to delete smth
        // 204 - No content, most common status code for successful delete action
        // authentication - who you are? you need to tell to the server who you are before getting any data
        // ALL HTTP STATUS CODES HAVE SAME MEANING EVERYWHERE
        // 201 - always after successful POST request
        // 200 - always after successful GET request
        // 204 - always after successful DELETE request
        // 4XX - always after unsuccessful request and it was your fault
        given().
                auth().basic("admin", "admin").
                baseUri(BASE_URL).
         when().
                delete("/api/spartans/{id}", 102).prettyPeek().
        then().
                statusCode(204);
    }
}
