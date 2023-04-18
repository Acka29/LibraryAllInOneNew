package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;

public class US01_APIStepDefs {

    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;

    Map<String, Object> newBookInfo;
    String pathParam;
    /**
     * US 01 RELATED STEPS
     *
     */
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {

        givenPart = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));
    }
    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        givenPart.accept(contentType);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint).prettyPeek();
        thenPart = response.then();
    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
        thenPart.statusCode(statusCode);
    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        thenPart.contentType(contentType);
    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        thenPart.body(path, everyItem(notNullValue()));
    }

    @Given("Path param is {string}")
    public void path_param_is(String pathParam) {

     givenPart.pathParam("id", pathParam);
     this.pathParam =pathParam;
    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String field) {
        thenPart.body(field, is(pathParam));
    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> fields) {

       JsonPath jsonPath = thenPart.extract().jsonPath();

        for (int i = 0; i < fields.size(); i++) {
            assertNotNull(jsonPath.getString(fields.get(i)));
        }

    }

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String requestContentType) {

       givenPart.headers("Content-Type",requestContentType);
    }
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String book) {

        Map<String, Object> newBookInfo = LibraryAPI_Util.getRandomBookMap();

        givenPart.formParams(newBookInfo);
    }

    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endPoint) {

        thenPart=givenPart.post(ConfigurationReader.getProperty("library.baseUri") + endPoint).prettyPeek().then();

    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String path1, String message) {

        thenPart.body(path1, equalTo(message));
        
    }


    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String path) {

    }
    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String string) {

    }
    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {

    }





}
