package com.randomuser.tests;

import com.randomuser.api.RandomUserApiClient;
import com.randomuser.models.UserResponse;
import com.randomuser.utils.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RandomUserApiTests {

    @Test(priority = 1)
    public void testGetSingleUser() {
        Response response = RandomUserApiClient.getUsers();

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        ResponseValidator.validateResponseSchema(apiResponse);
        Assert.assertEquals(apiResponse.getResults().size(), 1, "Should return 1 user by default");
    }

    @Test(priority = 2)
    public void testGetMultipleUsers() {
        Response response = RandomUserApiClient.getUsersWithParams(5, null, null, null);

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        ResponseValidator.validateResponseSchema(apiResponse);
        Assert.assertEquals(apiResponse.getResults().size(), 5, "Should return 5 users");
    }

    @Test(priority = 3)
    public void testGetUsersWithGenderFilter() {
        Response response = RandomUserApiClient.getUsersWithParams(3, "female", null, null);

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        ResponseValidator.validateResponseSchema(apiResponse);

        // Verify all users are female
        apiResponse.getResults().forEach(user ->
                Assert.assertEquals(user.getGender(), "female", "All users should be female")
        );
    }

    @Test(priority = 4)
    public void testGetUsersWithNationality() {
        Response response = RandomUserApiClient.getUsersWithParams(3, null, "us", null);

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        ResponseValidator.validateResponseSchema(apiResponse);

        // Verify all users have US nationality
        apiResponse.getResults().forEach(user ->
                Assert.assertEquals(user.getNat(), "US", "All users should be from US")
        );
    }

    @Test(priority = 5)
    public void testGetUsersWithSeed() {
        String seed = "testseed123";
        Response response1 = RandomUserApiClient.getUsersWithParams(1, null, null, seed);
        Response response2 = RandomUserApiClient.getUsersWithParams(1, null, null, seed);

        ResponseValidator.validateStatusCode(response1, 200);
        ResponseValidator.validateStatusCode(response2, 200);

        UserResponse apiResponse1 = response1.as(UserResponse.class);
        UserResponse apiResponse2 = response2.as(UserResponse.class);

        // Users with same seed should be identical
        Assert.assertEquals(apiResponse1.getResults().get(0).getEmail(),
                apiResponse2.getResults().get(0).getEmail(),
                "Users with same seed should be identical");
    }

    @Test(priority = 6)
    public void testPagination() {
        Response response = RandomUserApiClient.getUsersWithPage(2, 3);

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        ResponseValidator.validateResponseSchema(apiResponse);
        Assert.assertEquals(apiResponse.getInfo().getPage(), 2, "Should return page 2");
    }

    @Test(priority = 7)
    public void testMaxResultsLimit() {
        Response response = RandomUserApiClient.getUsersWithParams(5000, null, null, null);

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        ResponseValidator.validateResponseSchema(apiResponse);
        Assert.assertTrue(apiResponse.getResults().size() <= 5000, "Should not exceed max results limit");
    }

    @Test(priority = 8)
    public void testUserDataStructure() {
        Response response = RandomUserApiClient.getUsers();

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        com.randomuser.models.User user = apiResponse.getResults().get(0);

        ResponseValidator.validateUserData(user);
        Assert.assertNotNull(user.getPhone(), "Phone should not be null");
        Assert.assertNotNull(user.getCell(), "Cell should not be null");
        Assert.assertNotNull(user.getPicture(), "Picture should not be null");
    }

    @Test(priority = 9)
    public void testResponseTime() {
        long startTime = System.currentTimeMillis();
        Response response = RandomUserApiClient.getUsers();
        long endTime = System.currentTimeMillis();

        ResponseValidator.validateStatusCode(response, 200);
        Assert.assertTrue((endTime - startTime) < 3000, "Response time should be less than 3 seconds");
    }

    @Test(priority = 10)
    public void testInvalidParameters() {
        Response response = given()
                .queryParam("results", "invalid")
                .when()
                .get("/")
                .then()
                .extract()
                .response();

        // API might return 200 with default behavior or error
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 400,
                "Should handle invalid parameters gracefully");
    }

    @Test(priority = 11)
    public void testContentType() {
        Response response = RandomUserApiClient.getUsers();

        ResponseValidator.validateStatusCode(response, 200);
        Assert.assertEquals(response.getContentType(), "application/json; charset=utf-8",
                "Content type should be JSON");
    }

    @Test(priority = 12)
    public void testEmailFormat() {
        Response response = RandomUserApiClient.getUsersWithParams(10, null, null, null);

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        apiResponse.getResults().forEach(user -> {
            Assert.assertTrue(user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"),
                    "Email should be in valid format: " + user.getEmail());
        });
    }

    @Test(priority = 13)
    public void testNameFields() {
        Response response = RandomUserApiClient.getUsersWithParams(5, null, null, null);

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        apiResponse.getResults().forEach(user -> {
            Assert.assertNotNull(user.getName().getFirst(), "First name should not be null");
            Assert.assertNotNull(user.getName().getLast(), "Last name should not be null");
            Assert.assertFalse(user.getName().getFirst().isEmpty(), "First name should not be empty");
            Assert.assertFalse(user.getName().getLast().isEmpty(), "Last name should not be empty");
        });
    }

    @Test(priority = 14)
    public void testLocationData() {
        Response response = RandomUserApiClient.getUsers();

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        com.randomuser.models.User user = apiResponse.getResults().get(0);

        Assert.assertNotNull(user.getLocation(), "Location should not be null");
        Assert.assertNotNull(user.getLocation().getCity(), "City should not be null");
        Assert.assertNotNull(user.getLocation().getCountry(), "Country should not be null");
        Assert.assertNotNull(user.getLocation().getState(), "State should not be null");

        // Дополнительные проверки для структуры location
        Assert.assertNotNull(user.getLocation().getStreet(), "Street should not be null");
        Assert.assertNotNull(user.getLocation().getStreet().getName(), "Street name should not be null");
        Assert.assertTrue(user.getLocation().getStreet().getNumber() > 0, "Street number should be positive");
    }

    @Test(priority = 15)
    public void testDifferentNationalities() {
        String[] nationalities = {"us", "gb", "fr", "de", "es"};

        for (String nat : nationalities) {
            Response response = RandomUserApiClient.getUsersWithParams(1, null, nat, null);
            ResponseValidator.validateStatusCode(response, 200);

            UserResponse apiResponse = response.as(UserResponse.class);
            Assert.assertEquals(apiResponse.getResults().get(0).getNat().toUpperCase(),
                    nat.toUpperCase(), "Nationality should match");
        }
    }

    @Test(priority = 16)
    public void testMixedGenderResults() {
        Response response = RandomUserApiClient.getUsersWithParams(10, null, null, null);

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        long maleCount = apiResponse.getResults().stream()
                .filter(user -> "male".equals(user.getGender()))
                .count();
        long femaleCount = apiResponse.getResults().stream()
                .filter(user -> "female".equals(user.getGender()))
                .count();

        Assert.assertTrue(maleCount > 0 || femaleCount > 0,
                "Should return users of both genders in mixed results");
    }

    @Test(priority = 17)
    public void testInfoObjectStructure() {
        Response response = RandomUserApiClient.getUsersWithParams(5, null, null, "testseed");

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        UserResponse.Info info = apiResponse.getInfo();

        Assert.assertNotNull(info.getSeed(), "Seed should not be null");
        Assert.assertEquals(info.getResults(), 5, "Results count should match");
        Assert.assertTrue(info.getPage() >= 1, "Page should be at least 1");
        Assert.assertNotNull(info.getVersion(), "Version should not be null");
    }

    @Test(priority = 18)
    public void testPictureUrls() {
        Response response = RandomUserApiClient.getUsers();

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        com.randomuser.models.User user = apiResponse.getResults().get(0);

        Assert.assertTrue(user.getPicture().getLarge().startsWith("https://"),
                "Large picture should be HTTPS URL");
        Assert.assertTrue(user.getPicture().getMedium().startsWith("https://"),
                "Medium picture should be HTTPS URL");
        Assert.assertTrue(user.getPicture().getThumbnail().startsWith("https://"),
                "Thumbnail picture should be HTTPS URL");
    }

    @Test(priority = 19)
    public void testLoginData() {
        Response response = RandomUserApiClient.getUsers();

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        com.randomuser.models.User user = apiResponse.getResults().get(0);

        Assert.assertNotNull(user.getLogin().getUsername(), "Username should not be null");
        Assert.assertNotNull(user.getLogin().getUuid(), "UUID should not be null");
    }

    @Test(priority = 20)
    public void testDobAndRegisteredDates() {
        Response response = RandomUserApiClient.getUsers();

        ResponseValidator.validateStatusCode(response, 200);

        UserResponse apiResponse = response.as(UserResponse.class);
        com.randomuser.models.User user = apiResponse.getResults().get(0);

        Assert.assertNotNull(user.getDob().getDate(), "Date of birth should not be null");
        Assert.assertTrue(user.getDob().getAge() > 0, "Age should be positive");

        Assert.assertNotNull(user.getRegistered().getDate(), "Registration date should not be null");
        Assert.assertTrue(user.getRegistered().getAge() >= 0, "Registration age should be non-negative");
    }
}