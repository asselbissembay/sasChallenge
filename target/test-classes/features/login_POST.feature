  Feature: Test '/login' API by sending POST request

    Scenario: Successful login via a POST request to $BASE_URL/login
      Given post request is sent with valid credentials to the api endpoint "https://reqres.in/api/login"

      Scenario: Unsuccessful login through POST request
        Given post request is sent with incomplete credentials to the api endpoint "https://reqres.in/api/login"