package me.loki2302;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.*;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.udf.DefaultUDFFinder;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EvaluatorServiceTest {
    @Test
    public void canProvideAndReceiveContext() {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellFormula("GETCONTEXT(\"a\")"); // A1
        row.createCell(1).setCellFormula("GETCONTEXT(\"b\")"); // B1
        row.createCell(2).setCellFormula("PUTCONTEXT(\"sum\", A1+B1)"); // C1
        row.createCell(3).setCellFormula("PUTCONTEXT(\"diff\", A1-B1)"); // D1

        EvaluationService evaluationService = new EvaluationService();
        Map<String, Double> output = evaluationService.evaluate(workbook, new HashMap<String, Double>() {{
            put("a", 111.);
            put("b", 222.);
        }});

        assertEquals(2, output.size());
        assertEquals("333", NumberToTextConverter.toText(output.get("sum")));
        assertEquals("-111", NumberToTextConverter.toText(output.get("diff")));
    }

    public static class EvaluationService {
        public Map<String, Double> evaluate(Workbook workbook, Map<String, Double> input) {
            Map<String, Double> output = new HashMap<>();
            UDFFinder externalContextUdfLibrary = new DefaultUDFFinder(
                    new String[] {
                            "GETCONTEXT",
                            "PUTCONTEXT"
                    },
                    new FreeRefFunction[] {
                            new EvaluationService.GetContextFreeRefFunction(input),
                            new EvaluationService.PutContextFreeRefFunction(output)
                    });
            workbook.addToolPack(externalContextUdfLibrary);

            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

            return output;
        }

        private static class GetContextFreeRefFunction implements FreeRefFunction {
            private final Map<String, Double> context;

            private GetContextFreeRefFunction(Map<String, Double> context) {
                this.context = context;
            }

            @Override
            public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
                if(args.length != 1) {
                    return ErrorEval.VALUE_INVALID;
                }

                String name = OperandResolver.coerceValueToString(args[0]);
                return new NumberEval(context.get(name));
            }
        }

        private static class PutContextFreeRefFunction implements FreeRefFunction {
            private final Map<String, Double> context;

            private PutContextFreeRefFunction(Map<String, Double> context) {
                this.context = context;
            }

            @Override
            public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
                if(args.length != 2) {
                    return ErrorEval.VALUE_INVALID;
                }

                String name = OperandResolver.coerceValueToString(args[0]);
                try {
                    Double value = OperandResolver.coerceValueToDouble(args[1]);
                    context.put(name, value);

                    return new NumberEval(value);
                } catch (EvaluationException e) {
                    e.printStackTrace();
                    return e.getErrorEval();
                }
            }
        }
    }
}
