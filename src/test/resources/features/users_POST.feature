Feature: POST to $BASE_URL/users to create a new user


  Scenario: Create user and verify response is given with the expected JSON attributes
    Given a new user is created with its name and job
    Then validate the response includes the expected JSON attributes