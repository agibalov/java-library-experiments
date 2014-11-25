package me.loki2302;

import com.thoughtworks.qdox.JavaProjectBuilder
import com.thoughtworks.qdox.model.JavaClass
import com.thoughtworks.qdox.model.JavaSource
import org.junit.Test

import static org.junit.Assert.assertEquals

class QDoxTest {
    @Test
    void dummy() {
        JavaSource javaSource = new JavaProjectBuilder()
                .addSource(new StringReader('''
package me.loki2302;

public class Dummy {
}
'''))

        List<JavaClass> classes = javaSource.getClasses()
        assertEquals(1, classes.size())

        JavaClass dummyClass = classes.get(0)
        assertEquals('Dummy', dummyClass.getName())
        assertEquals('me.loki2302', dummyClass.getPackageName())
    }
}
