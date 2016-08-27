package me.loki2302;

import com.thoughtworks.qdox.JavaProjectBuilder
import com.thoughtworks.qdox.model.DocletTag
import com.thoughtworks.qdox.model.JavaClass
import com.thoughtworks.qdox.model.JavaMethod
import com.thoughtworks.qdox.model.JavaSource
import org.junit.Test

import static org.junit.Assert.*

class QDoxTest {
    @Test
    void dummy() {
        JavaSource javaSource = new JavaProjectBuilder()
                .addSource(new StringReader('''
package me.loki2302;

public class Dummy {
    public int addNumbers(float a, double b) {
        return a + b;
    }
}
'''))

        List<JavaClass> classes = javaSource.classes
        assertEquals(1, classes.size())

        JavaClass dummyClass = classes.first()
        assertEquals('Dummy', dummyClass.name)
        assertTrue(dummyClass.public)
        assertEquals('me.loki2302', dummyClass.packageName)

        assertEquals(1, dummyClass.methods.size())
        JavaMethod addNumbersMethod = dummyClass.methods.first()
        assertEquals('addNumbers', addNumbersMethod.name)
        assertTrue(addNumbersMethod.public)

        assertEquals('int', addNumbersMethod.returns.value)
        assertEquals('float', addNumbersMethod.parameters[0].type.value)
        assertEquals('double', addNumbersMethod.parameters[1].type.value)
    }

    @Test
    void checkHowItWorksWithJavadoc() {
        JavaSource javaSource = new JavaProjectBuilder().addSource(new File('src/main/java/me/loki2302/CalculatorService.java'))
        assertEquals(1, javaSource.classes.size())

        JavaClass calculatorServiceClass = javaSource.classes[0]
        assertEquals('CalculatorService', calculatorServiceClass.name)

        calculatorServiceClass.getClassNamePrefix()

        if(true) {
            DocletTag returnDocletTag = calculatorServiceClass.getTagByName('return')
            assertNull(returnDocletTag) // Sure class doesn't have a return tag

            assertEquals('Implements calculator functionality', calculatorServiceClass.comment)
        }

        assertEquals(2, calculatorServiceClass.methods.size())

        JavaMethod addNumbersMethod = calculatorServiceClass.methods[0]
        assertEquals('addNumbers', addNumbersMethod.name)

        if(true) {
            DocletTag returnDocletTag = addNumbersMethod.getTagByName('return')
            assertNotNull(returnDocletTag)
            assertEquals('sum of a and b', returnDocletTag.value)
        }

        if(true) {
            List<DocletTag> parameterDocletTags = addNumbersMethod.getTagsByName('param')
            assertEquals(2, parameterDocletTags.size())

            assertEquals(['a', 'number', 'A'], parameterDocletTags[0].parameters)
        }
    }
}
