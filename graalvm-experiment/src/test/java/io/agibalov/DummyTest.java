package io.agibalov;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void canExposeAPrimitiveValue() {
        try (Context context = Context.create()) {
            Value bindings = context.getBindings("js");
            bindings.putMember("x", 123);

            Value result = context.eval("js", "x + 1");
            assertEquals(124, result.asInt());
        }
    }

    @Test
    public void canExposeADto() {
        try (Context context = Context.newBuilder()
                .allowAllAccess(true)
                .build()) {

            TwoNumbers twoNumbers = new TwoNumbers();
            twoNumbers.a = 2;
            twoNumbers.b = 3;

            Value bindings = context.getBindings("js");
            bindings.putMember("dto", twoNumbers);

            Value result = context.eval("js", "dto.a + dto.b");
            assertEquals(5, result.asInt());
        }
    }

    @Test
    public void canExposeAnExecutable() {
        try (Context context = Context.create()) {
            Value bindings = context.getBindings("js");

            bindings.putMember("addNumbers", new ProxyExecutable() {
                @Override
                public Object execute(Value... arguments) {
                    return arguments[0].asInt() + arguments[1].asInt();
                }
            });

            Value result = context.eval("js", "addNumbers(2, 3)");
            assertEquals(5, result.asInt());
        }
    }

    @Test
    public void canExposeAService() {
        try (Context context = Context.newBuilder()
                .allowAllAccess(true)
                .build()) {

            CalculatorService calculatorService = new CalculatorService();

            Value bindings = context.getBindings("js");
            bindings.putMember("calc", calculatorService);

            Value result = context.eval("js", "calc.addNumbers(2, 3)");
            assertEquals(5, result.asInt());
        }
    }

    public static class TwoNumbers {
        public int a;
        public int b;
    }

    public static class CalculatorService {
        public int addNumbers(int a, int b) {
            return a + b;
        }
    }
}
