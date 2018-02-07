package me.loki2302;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class App extends SimpleApplication {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    private Geometry geometry;

    @Override
    public void simpleUpdate(float tpf) {
        geometry.rotate(0, tpf, 0);
    }

    @Override
    public void simpleInitApp() {
        Box box = new Box(1, 1, 1);
        geometry = new Geometry("Box", box);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Red);
        geometry.setMaterial(material);
        rootNode.attachChild(geometry);
    }
}
