package io.agibalov;

import com.eclipsesource.v8.V8;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class J2v8Test {
    @Test
    public void dummy() {
        V8 runtime = V8.createV8Runtime();
        try {
            int result = runtime.executeIntegerScript(""
                    + "var hello = 'hello, ';\n"
                    + "var world = 'world!';\n"
                    + "hello.concat(world).length;\n");
            assertEquals(13, result);
        } finally {
            runtime.release();
        }
    }
}
