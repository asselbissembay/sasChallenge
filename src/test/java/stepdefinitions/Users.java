package stepdefinitions;

import com.github.javafaker.Faker;
import helpers.ConfigurationReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

public class Users {

    Response response;
    List<Map<String,String>> completeData = new ArrayList<>();
    @Given("send a GET request and verify the response and report on the number of users returned")
    public void sendAGETRequestAndVerifyTheResponseAndReportOnTheNumberOfUsersReturned() {

        String api_endpoint_page1 = ConfigurationReader.getProperty("BASE_URL") + "/users?page=1";
        System.out.println("The endpoint is for page 1: " + api_endpoint_page1);

        response = given().header("Content-Type", ContentType.JSON,"Accept",ContentType.JSON)
                .when()
                .get(api_endpoint_page1)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        JsonPath jsonPath1 = new JsonPath(response.prettyPrint());

        System.out.println("The status received for page 1: " + response.statusLine());
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println("The status code successfully verified");

        List<Map<String,String>> list1 = jsonPath1.getList("data");
        System.out.println("The number of users is in the 1st page: " + list1.size());

        System.out.println("============================ page 2 ====================================");

        String api_endpoint_page2 = ConfigurationReader.getProperty("BASE_URL") + "/users?page=2";
        System.out.println("The endpoint is for page 2: " + api_endpoint_page2);

        response = given().header("Content-Type", ContentType.JSON,
                        "Accept",ContentType.JSON)
                .when()
                .get(api_endpoint_page2)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        JsonPath jsonPath2 = new JsonPath(response.prettyPrint());

        System.out.println("The status received for page 2: " + response.statusLine());
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println("The status code successfully verified");

        List<Map<String, String>> list2 = jsonPath2.getList("data");
        System.out.println("The number of users is in the 2nd page: " + list2.size());

        completeData.addAll(list1);
        completeData.addAll(list2);
        System.out.println("The number of users is in the complete list: " + completeData.size());
        System.out.println("The users' full names:");
        for (int i = 0; i < completeData.size(); i++) {
            String firstName = completeData.get(i).get("first_name");
            String lastName = completeData.get(i).get("last_name");
            System.out.println((i+1) + ". " + firstName + " " + lastName);
        }
    }

    @Given("send a GET request and verify response and correctly structured JSON is returned for a specific, valid user.")
    public void sendAGETRequestAndVerifyResponseAndCorrectlyStructuredJSONIsReturnedForASpecificValidUser() {

        sendAGETRequestAndVerifyTheResponseAndReportOnTheNumberOfUsersReturned();
        int randomNum = ThreadLocalRandom.current().nextInt(1, 12 + 1);
        String api_endpoint = ConfigurationReader.getProperty("BASE_URL") + "/users/" + randomNum;
        System.out.println("The endpoint for randomly picked user_id is ==> " + api_endpoint);

        response = given().header("Content-Type", ContentType.JSON,
                        "Accept",ContentType.JSON)
                .when()
                .get(api_endpoint)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        JsonPath jsonPath = new JsonPath(response.prettyPrint());
        Map<String, String> singleUser = jsonPath.get("data");
        System.out.println("single user: " + singleUser);
        Assert.assertTrue(completeData.contains(singleUser));
        System.out.println("The picked single user exists in the complete list");
    }

    @Given("send a GET request and verify response when an invalid user is requested")
    public void sendAGETRequestAndVerifyResponseWhenAnInvalidUserIsRequested() {
        int randomNum = ThreadLocalRandom.current().nextInt(13, 100);
        String api_endpoint = ConfigurationReader.getProperty("BASE_URL") + "/users/" + randomNum;
        System.out.println("The endpoint for randomly picked user_id is ==> " + api_endpoint);

        response = given().header("Content-Type", ContentType.JSON,
                        "Accept",ContentType.JSON)
                .when()
                .get(api_endpoint)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();
        response.prettyPrint();

        System.out.println("The status received: " + response.statusLine());
        Assert.assertEquals(404, response.getStatusCode());
        System.out.println("The status code successfully verified");
    }

    Faker faker = new Faker();
    String firstName = faker.name().firstName();
    String jobPosition = faker.job().position();
    @Given("a new user is created with its name and job")
    public void aNewUserIsCreatedWithItsNameAndJob() {
        String users_endpoint = ConfigurationReader.getProperty("BASE_URL") + "/users";
        System.out.println("The endpoint is for users api: " + users_endpoint);

        String requestBody = "{\n" +
                "  \"name\": \"" + firstName + "\",\n" +
                "  \"job\": \"" + jobPosition + "\"\n}";
        System.out.println(requestBody);

        response = given().header("Content-Type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post(users_endpoint)
                .then()
                .extract()
                .response();
//        response.prettyPrint();
    }

    @Then("validate the response includes the expected JSON attributes")
    public void validateTheResponseIncludesTheExpectedJSONAttributes() {
        System.out.println("The status received: " + response.statusLine());
        Assert.assertEquals(201, response.getStatusCode());
        System.out.println("The status code successfully verified");

        JsonPath jsonPath = new JsonPath(response.prettyPrint());

        Assert.assertEquals(firstName, jsonPath.get("name"));
        System.out.println("Name of the newly created user verified");

        Assert.assertEquals(jobPosition, jsonPath.get("job"));
        System.out.println("Job of the newly created user verified");

    }

    @Given("the newly created user is deleted and verify the response")
    public void theNewlyCreatedUserIsDeletedAndVerifyTheResponse() {

        String userDelete_endpoint = ConfigurationReader.getProperty("BASE_URL") + "/users/2";
        System.out.println("The endpoint is for users api: " + userDelete_endpoint);

        response = given().header("Content-type", "application/json")
                .when()
                .delete(userDelete_endpoint)
                .then()
                .extract()
                .response();

        System.out.println("The status received: " + response.statusLine());
        Assert.assertEquals(204, response.getStatusCode());
        System.out.println("The status code successfully verified");
    }
}
