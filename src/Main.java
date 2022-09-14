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
        String imagePath = "C:\\Users\\Daniel\\Pictures\\ZachoRaisedBrow.png";
        BufferedImage picture = ImageIO.read(new File(imagePath));

        Manager manager = new Manager();
        //manager.deleteAllLevels();

        Level zacho = manager.getLevel("zacho image");
        if (zacho == null) zacho = manager.createLevel("zacho image");
        zacho.deleteObjects();

        Color col = new Color(zacho.nextFreeColorChannel());
        col.setRGB(255, 0, 0);
        zacho.addColorChannel(col);

        int resolution = 141;
        int sampleDistance = Math.min(picture.getWidth(), picture.getHeight()) / resolution;
        int pictureGroup = zacho.nextFreeGroup();
        System.out.println("Picture group ID: " + pictureGroup);

        float offsetX = zacho.getLastObjectPosition().x + 300;
        float offsetY = 15;
        for (int y = 0; y < resolution; y++) {
            for (int x = 0; x < resolution; x++) {
                GDObject obj = new GDObject(211, x * 3 + 285 + offsetX, y * -3 + (75 + resolution*3) + offsetY);
                obj.setScale(0.1f);
                obj.addGroup(pictureGroup);
                obj.setColorChannel(col);
                obj.setEditorLayer(101);

                obj.getHSB().setRGB(picture.getRGB(x * sampleDistance, y * sampleDistance));

                zacho.addObject(obj);
            }
        }

        //Fix object density
        int objectCount = zacho.getObjectCount();
        float minLastX = (float)objectCount / 115f * 30f;  //115 is the amount of objects per block. This may differ; I have no idea how this works other than it won't start the level with this.
        if (minLastX > zacho.getLastObjectPosition().x) {  //Also, the actual value is closer to 115,843.
            zacho.addObject(1, minLastX, 2100);
        }

        manager.save();
//        Manager.copy(new String(manager.constructSaveFile(), StandardCharsets.UTF_8));
    }
}
