package com.randomuser.api;

import com.randomuser.utils.TestConfig;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RandomUserApiClient {

    static {
        RestAssured.baseURI = TestConfig.getBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static io.restassured.response.Response getUsers() {
        return given()
                .when()
                .get("/")
                .then()
                .extract()
                .response();
    }

    public static io.restassured.response.Response getUsersWithParams(int results, String gender, String nat, String seed) {
        RequestSpecification request = given();

        if (results > 0) {
            request.queryParam("results", results);
        }
        if (gender != null && !gender.isEmpty()) {
            request.queryParam("gender", gender);
        }
        if (nat != null && !nat.isEmpty()) {
            request.queryParam("nat", nat);
        }
        if (seed != null && !seed.isEmpty()) {
            request.queryParam("seed", seed);
        }

        return request
                .when()
                .get("/")
                .then()
                .extract()
                .response();
    }

    public static io.restassured.response.Response getUsersWithPage(int page, int results) {
        return given()
                .queryParam("page", page)
                .queryParam("results", results)
                .when()
                .get("/")
                .then()
                .extract()
                .response();
    }
}