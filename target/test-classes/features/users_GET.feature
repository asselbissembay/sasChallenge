
Feature: Get a list of users

  Scenario: GET a list of users from the $BASE_URL/users endpoint
    Given send a GET request and verify the response and report on the number of users returned
