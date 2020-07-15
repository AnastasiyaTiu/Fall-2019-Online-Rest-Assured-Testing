package com.automation.tests.day6;

import com.automation.utilities.ConfigurationReader;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.automation.pojos.Spartan;
import com.google.gson.Gson;
import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;

public class POJOPracticeWithSpartanApp {
    @BeforeAll
    public static void beforeAll(){
        baseURI = ConfigurationReader.getProperty("SPARTAN.URI");
        authentication = basic("admin", "admin");
    }
    /*
    {
  "gender": "Female",
  "name": "Anastasiya",
  "phone": 23232312129
}
     */
    @Test
    public void addSpartanTest(){
        Map<String, String> spartan = new HashMap<>();
        spartan.put("gender", "Female");
        spartan.put("name", "Anastasiya");
        spartan.put("phone", "23232312129");

        RequestSpecification requestSpecification = given().
                                                        auth().basic("admin", "admin").
                                                        contentType(ContentType.JSON).
                                                        accept(ContentType.JSON).
                                                        body(spartan);

        Response response = given().
                                    auth().basic("admin", "admin").
                                    contentType(ContentType.JSON).
                                    accept(ContentType.JSON).
                                    body(spartan).
                            when().
                                   post("/spartans").prettyPeek();

        response.then().statusCode(201);
        response.then().body("success", is("A Spartan is Born!"));

        // deserialization
        Spartan spartanResponse = response.jsonPath().getObject("data", Spartan.class);
        Map<String, Object> spartanResponseMap = response.jsonPath().getObject("data", Map.class);

        System.out.println(spartanResponse);
        System.out.println(spartanResponseMap);

        // spartanResponse is a Spartan
        System.out.println(spartanResponse instanceof Spartan); // must be true
    }

    @Test
    @DisplayName("Retrieve existing user, update his name and verify that name was updated successfully.")
    public void updateSpartanTest(){
        int userToUpdate = 117;
        String name = "Ana";

        //HTTP PUT request to update exiting record, for example exiting spartan.
        // PUT - requires to provide ALL parameters in body

        Spartan spartan = new Spartan(name, "Female", 23232312129L);

        // get spartan from web service
        Spartan spartanToUpdate = given().
                                          auth().basic("admin", "admin").
                                          accept(ContentType.JSON).
                                   when().
                                          get("/spartans/{id}", userToUpdate).as(Spartan.class);
        // update property that you need without affecting other properties
        System.out.println("Before update: " + spartanToUpdate);
        spartanToUpdate.setName(name); // change only name
        System.out.println("After update: " + spartanToUpdate);

        // request to update existing user with is 117
        Response response = given().
                                   auth().basic("admin", "admin").
                                   contentType(ContentType.JSON).
                                   body(spartanToUpdate).
                             when().
                                   put("/spartans/{id}", userToUpdate).prettyPeek();

        // verify that status code is 204 after update
        response.then().statusCode(204);
        System.out.println("#####################");
        // to get user with id 101, the one that we've just updated
        given().
                auth().basic("admin", "admin").
        when().
                get("/spartans/{id}", userToUpdate).prettyPeek().
        then().
                statusCode(200).body("name", is(name));
        // verify that name is correct, after update

            }


}
