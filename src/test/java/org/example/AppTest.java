package org.example;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.json.JSONObject;

//import org.junit.Test;
import io.qameta.allure.Description;
//import sun.jvm.hotspot.HelloWorld;

/**
 * Unit test using Rest Assured library.
 */
public class AppTest 
{
    static Map<String, String> headers = new HashMap<>();
    String BaseUrl = "https://reqres.in/api/users"; //base Url for request
    String UserFirstName;
    String UserId;

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        Logger logger = LoggerFactory.getLogger(AppTest.class);
        logger.info("Testing class started");
    }

    @BeforeAll
    static void beforeAll() {
    //for logging request and responses in Allure reporting
        RestAssured.filters(new AllureRestAssured());
    }

    /**------------------------------------------------
     * Test GET commands
     *------------------------------------------------/
    /**
     * Check if user with Id=1 is exist
     * and print put on the screen his first name
     */
    @Test
    public void getUserId(){
        UserId = "id=1";
        String requestPath = BaseUrl + "?" + UserId;

        String firstName = given()
                .log()
                .all()
                .when()
                .request("GET", requestPath)
                .prettyPeek()
                .then()
                .statusCode(200).extract().response().jsonPath().get("data.first_name");

        System.out.println ("User found with first name: " + firstName);
    }


    /**
     * Check if user with Id=1 is exist
     * and print put on the screen his first and last name
     */
    @Test
    public void getUserName() {
        UserId = "id=1";
        String requestPath = BaseUrl + "?" + UserId;

        String firstName = given()
                .log()
                .all()
                .when()
                .get(requestPath)
                .then().assertThat().body("data.first_name", equalTo("George"))
                  .statusCode(200).extract().response().jsonPath().get("data.first_name");

        String lastName = given()
                .log()
                .all()
                .when()
                .get(requestPath)
                .then().assertThat().body("data.last_name", equalTo("Bluth"))
                .statusCode(200).extract().response().jsonPath().get("data.last_name");

        String UserName = firstName + " " + lastName;

        System.out.println("User found: " + UserName);
    }

    /**
     * Check for user with not valid Id
     * and print put on the screen his first and last name
     */
    @Test
    public void getResponseIfNotValidUserId() {
        UserId = "id=a"; //non-existent user id
        String requestPath = BaseUrl + "?" + UserId;
        Response resp=RestAssured.get(requestPath);
        int code=resp.getStatusCode();

        System.out.println("User with Id not found, not valid id: " + UserId);
        System.out.println("Web page returned code: " + code);
    }
    /**------------------------------------------------
     * Test POST commands
     *------------------------------------------------/
    /**
     * Check for user with not valid Id
     * and print put on the screen his first and last name
     */
    @Test
    public void postNewUserWithJsonBody() {
        String requestPath = BaseUrl;
        //String requestPath = "https://reqres.in/api/users";

        JSONObject requestBody = new JSONObject()
                .put("data.last_name", "Jim")
                .put("data.last_name", "Gandolfini");

        RequestSpecification request = given();
                request.header("Content-Type", "application/json");
                request.body(requestBody.toString());

        Response response = given()
                .body(requestBody.toString())
                .log()
                .all()
                .when()
                .post(requestPath)
                .then()
                .statusCode(201)
                .extract().response();
        response.prettyPrint();
        String origin = response.path("id");
        System.out.println(origin);
    }

}