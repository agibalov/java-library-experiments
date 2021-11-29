package io.agibalov;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeHelloResourceTest extends HelloResourceTest {
    // Execute the same tests but in native mode.
}
