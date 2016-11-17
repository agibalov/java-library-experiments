Feature: dummy
  It should work.

  Scenario: Eat apples and see what happens
    Given I have 10 apples
    When I eat 3 apples
    Then I still have 7 apples

  Scenario: Eat more apples and see what happens
    Given I have 10 apples
    When I eat 5 apples
    Then I still have 3 apples
