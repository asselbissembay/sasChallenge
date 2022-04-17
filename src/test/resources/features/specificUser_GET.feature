
Feature: Get information on a specific user

Scenario: GET information on a specific user
Given send a GET request and verify response and correctly structured JSON is returned for a specific, valid user.


Scenario: Verify response when an invalid user is requested
Given send a GET request and verify response when an invalid user is requested