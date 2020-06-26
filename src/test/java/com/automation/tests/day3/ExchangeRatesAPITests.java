package com.automation.tests.day3;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class ExchangeRatesAPITests {

    @BeforeAll // must be static
    public static void setup(){
        // for every single request this is a base URI
        baseURI = "http://api.openrates.io";
    }

    // get latest currency rates

    @Test
    public void getLatestRates(){
        // after ? we specify query parameters. If there are couple of them we use & concatenate them.
        // http://www.google.com/index.html?q=apple&zip=123123
        // q - query parameter
        // zip - another query parameter
        // with rest assured, we provide query parameters into given() part
        // give() - request preparation
        // you can specify query parameters in URL explicitly: http://api.openrates.io/latest?base=USD
        // rest assured, will just assemble URL for you
        Response response = given().
                                 queryParam("base", "USD").
                            when().
                                get("/latest").prettyPeek();

      // to read header of the response
        Headers headers = response.getHeaders(); // all headers
        String contentType = headers.getValue("Content-Type");
        System.out.println(contentType);

        // verify that GET request to the endpoint was successful
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("base", is("USD"));

        // is = same as equalTo
        // let's verify that response contains today's date
        // this line returns today's date in the required format : yyyy-MM-dd
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        response.then().assertThat().body("date", containsString(date));
    }

    // get history of rates for 2008
    @Test
    public void getHistoryOfRates(){
        Response response = given().
                                  queryParam("base", "USD").
                            when().
                                  get("2008-01-02").prettyPeek();
        Headers headers = response.getHeaders(); // response header

        response.then().assertThat().
                                    statusCode(200).
                               and().
                                    body("date", is("2008-01-02")).
                               and().
                                    body("rates.USD", is(1.0f));
        // and() doesn't have functional role it's just a syntax sugar
        // we can chain validations

       // rates - it's an object
       // all currencies are like instance variables
       // to get any instance variable(property), objectName.propertyName
       float actual = response.jsonPath().get("rates.USD");

       assertEquals(1.0, actual);
       System.out.println(actual);

       // we use jsonPath() to parse JSON response, retrieve some values
        /*
     * Get a JsonPath view of the response body. This will let you use the JsonPath syntax to get values from the response.
     * Example:
     * <p>
     * Assume that the GET request (to <tt>http://localhost:8080/lotto</tt>) returns JSON as:
     * <pre>
     * {
     * "lotto":{
     *   "lottoId":5,
     *   "winning-numbers":[2,45,34,23,7,5,3],
     *   "winners":[{
     *     "winnerId":23,
     *     "numbers":[2,45,34,23,3,5]
     *   },{
     *     "winnerId":54,
     *     "numbers":[52,3,12,11,18,22]
     *   }]
     *  }
     * }
         */


    }

}
