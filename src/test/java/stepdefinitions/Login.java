package stepdefinitions;

import com.fasterxml.jackson.databind.util.JSONPObject;
import helpers.ConfigurationReader;
import io.cucumber.gherkin.internal.com.eclipsesource.json.JsonObject;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.junit.Cucumber;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;

public class Login {

    Response response;

    @Given("post request is sent with valid credentials to the api endpoint {string}")
    public void post_request_is_sent_with_valid_credentials_to_the_api_endpoint(String api_endpoint) {

        RestAssured.baseURI = api_endpoint;
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "eve.holt@reqres.in");
        requestParams.put("password", "cityslicka");

        System.out.println("requestParams =====> " + requestParams);

        response = given().header("Content-Type", "application/json")
                .and()
                .body(requestParams)
                .when()
                .post(api_endpoint)
                .then()
                .extract()
                .response();

        response.prettyPrint();
//        RequestSpecification request = RestAssured.given();
//        request.header("Content-Type", "application/json");
//        request.body(requestParams.toJSONString());
//        response = request.post(api_endpoint);
        System.out.println("===============");
        System.out.println("The status received: " + response.statusLine());
        Assert.assertEquals(Integer.parseInt("200"), response.getStatusCode());
        System.out.println("The status code successfully verified");
    }

    @Given("post request is sent with incomplete credentials to the api endpoint {string}")
    public void postRequestIsSentWithIncompleteCredentialsToTheApiEndpoint(String api_endpoint) {

        RestAssured.baseURI = api_endpoint;
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "peter@klaven");

        System.out.println("requestParams =====> " + requestParams);

        response = given().header("Content-Type", "application/json")
                .and()
                .body(requestParams)
                .when()
                .post(api_endpoint)
                .then()
                .extract()
                .response();

        response.prettyPrint();

        System.out.println("The status received: " + response.statusLine());
        Assert.assertEquals(Integer.parseInt("400"), response.getStatusCode());
        System.out.println("The status code successfully verified");
    }

}
