Scenario: eating 3 of 10 apples
Given I have 10 apples
When I eat 3 apples
Then I have 7 apples

Scenario: eating all apples
Given I have 5 apples
When I eat 5 apples
Then I have no apples
