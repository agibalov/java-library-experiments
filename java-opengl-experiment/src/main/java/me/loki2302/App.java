package me.loki2302;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;

public class App {
    public static void main(String[] args) {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        GLCanvas glCanvas = new GLCanvas(glCapabilities);        
        
        Frame frame = new Frame("hello gl");
        frame.setSize(400, 300);
        frame.add(glCanvas);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }            
        });
        
        glCanvas.addGLEventListener(new MyScene());
        FPSAnimator animator = new FPSAnimator(glCanvas, 60);
        animator.start();        
    }
    
    public static class MyScene implements GLEventListener {
        private float phi = 0.f;

        public void init(GLAutoDrawable drawable) {            
        }

        public void dispose(GLAutoDrawable drawable) {           
        }

        public void display(GLAutoDrawable drawable) {
            phi += 0.3;                       
            
            GL2 gl = drawable.getGL().getGL2();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);
            gl.glLoadIdentity();
            
            gl.glRotatef(phi, 0, 0, 1);                       

            gl.glBegin(GL.GL_TRIANGLES);
            gl.glColor3f(1, 0, 0);
            gl.glVertex2f(-1, -1);
            gl.glColor3f(0, 1, 0);
            gl.glVertex2f(0, 1);
            gl.glColor3f(0, 0, 1);
            gl.glVertex2f(1, -1);
            gl.glEnd();            
        }

        public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {            
        }
    }
}
