package io.agibalov;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeCalculatorResourceTest extends CalculatorResourceTest {
    // Execute the same tests but in native mode.
}
