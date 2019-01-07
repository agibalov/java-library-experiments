package me.loki2302.feature.dummy;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

public class AppleSteps {
    private int appleCount;

    @Given("I have (\\d+) apples")
    public void givenIHaveNApples(int apples) {
        appleCount = apples;
    }

    @When("I eat (\\d+) apples")
    public void whenIEatNApples(int apples) {
        appleCount -= apples;
    }

    @Then("I still have (\\d+) apples")
    public void thenIStillHaveNApples(int apples) {
        assertEquals(apples, appleCount);
    }
}
