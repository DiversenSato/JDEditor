import diversanto.gdmanager.GDObject;
import diversanto.gdmanager.Level;
import diversanto.gdmanager.Manager;
import diversanto.gdmanager.color.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        Manager manager = new Manager();
        //Manager.copy(new String(manager.constructSaveFile(), StandardCharsets.UTF_8));

        Level imageTest = manager.getLevel("Image test", true);
        Level.reset(imageTest);
        imageTest.deleteObjects();
        imageTest.createImage("C:\\Users\\diver\\Pictures\\magnus.png", 150, 0);

        //manager.save();
    }
}
