import diversanto.gdmanager.Constants;
import diversanto.gdmanager.GDObject;
import diversanto.gdmanager.Level;
import diversanto.gdmanager.Manager;
import diversanto.gdmanager.color.Color;
import diversanto.gdmanager.color.HSB;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Main {
    private static final Random rand = new Random();

    public static void main(String[] args) throws Exception {
//        String imagePath = "C:\\Users\\diver\\Pictures\\magnus.png";
//        BufferedImage picture = ImageIO.read(new File(imagePath));

        Manager manager = new Manager();

//        manager.deleteAllLevels();
//        Level imageLevel = manager.createLevel("image test");
//
//        Color col = new Color(1);
//        col.setRGB(255, 0, 0);
//        imageLevel.addColorChannel(col);

//        int resolution = 54;
//        int sampleDistance = Math.min(picture.getWidth(), picture.getHeight()) / resolution;
//        for (int y = 0; y < resolution; y++) {
//            for (int x = 0; x < resolution; x++) {
//                GDObject obj = new GDObject(211, x * 3 + 285, y * -3 + (75 + resolution*3));
//                obj.setScale(0.1f);
//                obj.addGroup(1);
//                obj.setColorChannel(col);
//
//                obj.getHSB().setRGB(picture.getRGB(x * sampleDistance, y * sampleDistance));
//
//                imageLevel.addObject(obj);
//            }
//        }

        if (!Manager.debug) manager.save();
        if (Manager.debug) manager.constructSaveFile();
    }
}
