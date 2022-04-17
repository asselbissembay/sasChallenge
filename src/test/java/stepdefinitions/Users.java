package stepdefinitions;

import helpers.ConfigurationReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
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

    List<Map<String,String>> completeData = new ArrayList<>();
    @Given("send a GET request and verify the response and report on the number of users returned")
    public void sendAGETRequestAndVerifyTheResponseAndReportOnTheNumberOfUsersReturned() {

        String api_endpoint_page1 = ConfigurationReader.getProperty("BASE_URL") + "/users?page=1";
        System.out.println("The endpoint is for page 1: " + api_endpoint_page1);

        Response response1 = given().header("Content-Type", ContentType.JSON,"Accept",ContentType.JSON)
                .when()
                .get(api_endpoint_page1)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        JsonPath jsonPath1 = new JsonPath(response1.prettyPrint());

        System.out.println("The status received for page 1: " + response1.statusLine());
        Assert.assertEquals(Integer.parseInt("200"), response1.getStatusCode());
        System.out.println("The status code successfully verified");

        List<Map<String,String>> list1 = jsonPath1.getList("data");
        System.out.println("The number of users is in the 1st page: " + list1.size());

        System.out.println("============================ page 2 ====================================");

        String api_endpoint_page2 = ConfigurationReader.getProperty("BASE_URL") + "/users?page=2";
        System.out.println("The endpoint is for page 2: " + api_endpoint_page2);

        Response response2 = given().header("Content-Type", ContentType.JSON,
                        "Accept",ContentType.JSON)
                .when()
                .get(api_endpoint_page2)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

        JsonPath jsonPath2 = new JsonPath(response2.prettyPrint());

        System.out.println("The status received for page 2: " + response2.statusLine());
        Assert.assertEquals(Integer.parseInt("200"), response2.getStatusCode());
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

        Response response = given().header("Content-Type", ContentType.JSON,
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
}
