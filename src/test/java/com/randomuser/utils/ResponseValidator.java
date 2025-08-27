package com.randomuser.utils;

import com.randomuser.models.UserResponse;
import io.restassured.response.Response;
import org.testng.Assert;

public class ResponseValidator {

    public static void validateStatusCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
                "Unexpected status code");
    }

    public static void validateResponseSchema(UserResponse apiResponse) {
        Assert.assertNotNull(apiResponse, "Response should not be null");
        Assert.assertNotNull(apiResponse.getResults(), "Results should not be null");
        Assert.assertNotNull(apiResponse.getInfo(), "Info should not be null");
        Assert.assertTrue(apiResponse.getResults().size() > 0, "Results should contain users");
    }

    public static void validateUserData(com.randomuser.models.User user) {
        Assert.assertNotNull(user.getGender(), "Gender should not be null");
        Assert.assertNotNull(user.getName(), "Name should not be null");
        Assert.assertNotNull(user.getEmail(), "Email should not be null");
        Assert.assertNotNull(user.getLocation(), "Location should not be null");

        // Validate email format
        Assert.assertTrue(user.getEmail().contains("@"), "Email should contain @");
    }
}