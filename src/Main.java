import diversanto.gdmanager.Constants;
import diversanto.gdmanager.GDObject;
import diversanto.gdmanager.Level;
import diversanto.gdmanager.Manager;
import diversanto.gdmanager.color.Color;
import diversanto.gdmanager.color.HSB;

import java.util.Random;

public class Main {
    private static final Random rand = new Random();

    public static void main(String[] args) throws Exception {
        Manager manager = new Manager();

        manager.deleteLevel("image test");
        Level image = manager.createLevel("image test");

        Color col = new Color(1);
        col.setRGB(255, 255, 255);
        image.addColorChannel(col);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int colCH = x + y*10;

                GDObject obj = new GDObject(211, x * 30 + 285, y * 30 + 75);
                obj.setColorChannel(col);
                obj.getHSB().setRGB(colCH);
                image.addObject(obj);
            }
        }

        //manager.save();
    }
}
