import diversanto.gdmanager.GDObject;
import diversanto.gdmanager.Level;
import diversanto.gdmanager.Manager;
import diversanto.gdmanager.color.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        String imagePath = "C:\\Users\\diver\\Pictures\\magnus.png";
        BufferedImage picture = ImageIO.read(new File(imagePath));

        Manager manager = new Manager();

        Level indihome = manager.getLevel("Indihome 4 of 9");
        indihome.deleteGroup(15);

        Color col = new Color(indihome.nextFreeColorChannel());
        col.setRGB(255, 0, 0);
        indihome.addColorChannel(col);

        int resolution = 54;
        int sampleDistance = Math.min(picture.getWidth(), picture.getHeight()) / resolution;
        int pictureGroup = indihome.nextFreeGroup();
        System.out.println("Picture group ID: " + pictureGroup);

        int offsetX = 5955;
        int offsetY = 1245;
        for (int y = 0; y < resolution; y++) {
            for (int x = 0; x < resolution; x++) {
                GDObject obj = new GDObject(211, x * 3 + 285 + offsetX, y * -3 + (75 + resolution*3) + offsetY);
                obj.setScale(0.1f);
                obj.addGroup(pictureGroup);
                obj.setColorChannel(col);
                obj.setEditorLayer(101);

                obj.getHSB().setRGB(picture.getRGB(x * sampleDistance, y * sampleDistance));

                indihome.addObject(obj);
            }
        }

        manager.save();
        //Manager.copy(new String(manager.constructSaveFile(), StandardCharsets.UTF_8));
    }
}
