package me.loki2302;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.junit.Assert.assertEquals;

public class AppleSteps {
    private int appleCount;

    @Given("I have no apples")
    public void noApples() {
        appleCount = 0;
    }

    @Given("I have $count apples")
    public void someApples(int count) {
        appleCount = count;
    }

    @When("I eat $count apples")
    public void eat(int count) {
        appleCount -= count;
    }

    @Then("I have no apples")
    public void haveNoApples() {
        assertEquals(0, appleCount);
    }

    @Then("I have $count apples")
    public void haveSomeApples(int count) {
        assertEquals(count, appleCount);
    }
}
