Feature: Login Functionality

  Scenario: Successful login
    Given I am on the login page
    When I enter my username and password
    And I click the login button
    Then I should be redirected to the dashboard

#  Scenario: Invalid login
#    Given I am on the login page
#    When I enter invalid username and password
#    And I click the login button
#    Then I should see an error message
#    And I should stay on the login page
