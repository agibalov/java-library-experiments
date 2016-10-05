package me.loki2302;

import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

public class App {
    public static void main(String[] args) {
        SpoonAPI spoon = new Launcher();
        spoon.addInputResource("src/main/java");
        spoon.buildModel();

        for(CtType<?> ctType : spoon.getFactory().Class().getAll()) {
            System.out.printf("%s\n", ctType.getSimpleName());

            for(CtMethod<?> ctMethod : ctType.getAllMethods()) {
                if(!ctMethod.getSimpleName().equals("main")) {
                    continue;
                }

                System.out.printf("  %s\n", ctMethod.getSimpleName());

                CtBlock<?> body = ctMethod.getBody();
                for(CtStatement ctStatement : body.getStatements()) {
                    System.out.printf("    %s\n", ctStatement);
                }

                CtStatement firstStatement = body.getStatement(0);
                System.out.printf("%s\n", firstStatement.getClass());

                CtLocalVariable ctLocalVariable = (CtLocalVariable)firstStatement;
                System.out.printf("name=%s, type=%s, init-expr=%s\n",
                        ctLocalVariable.getSimpleName(),
                        ctLocalVariable.getType().getSimpleName(),
                        ctLocalVariable.getAssignment());
            }
        }
    }
}
