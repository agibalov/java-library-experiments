package me.loki2302;

import com.thoughtworks.qdox.JavaProjectBuilder
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
}
