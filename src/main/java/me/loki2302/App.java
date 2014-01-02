package me.loki2302;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        MethodNode mainMethodNode = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        
        mainMethodNode.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        mainMethodNode.instructions.add(new LdcInsnNode("Hello World!!!11"));
        mainMethodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
        mainMethodNode.instructions.add(new InsnNode(Opcodes.RETURN));
                
        ClassNode classNode = new ClassNode();
        classNode.version = Opcodes.V1_5;
        classNode.access = Opcodes.ACC_PUBLIC;
        classNode.name = "NewApp";
        classNode.superName = "java/lang/Object";
        classNode.methods.add(mainMethodNode);
        
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
                
        FileOutputStream classFileOutputStream = new FileOutputStream("NewApp.class");
        try {
            classFileOutputStream.write(classWriter.toByteArray());
        } finally {
            classFileOutputStream.close();
        }
        
        MyClassLoader myClassLoader = new MyClassLoader(classWriter.toByteArray());
        Class<?> newAppClass = myClassLoader.loadClass("NewApp");
        Method mainMethod = newAppClass.getMethod("main", String[].class);        
        mainMethod.invoke(null, new Object[] { new String[]{} });
    }
    
    public static class MyClassLoader extends ClassLoader {
        private final byte[] newAppClassBytes;
        
        public MyClassLoader(byte[] newAppClassBytes) {
            this.newAppClassBytes = newAppClassBytes;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if(name.equals("NewApp")) {
                return defineClass("NewApp", newAppClassBytes, 0, newAppClassBytes.length);
            }
            
            return super.findClass(name);
        }        
    }
}
