import diversanto.gdmanager.Constants;
import diversanto.gdmanager.GDObject;
import diversanto.gdmanager.Level;
import diversanto.gdmanager.Manager;

import java.util.Random;

public class Main {
    private static final Random rand = new Random();
    private static OpenSimplexNoise noise = new OpenSimplexNoise(rand.nextLong());
    public static void main(String[] args) throws Exception {
        Manager manager = new Manager();

        String lvlName = "Ship test";
        Level lvl = manager.getLevel(lvlName);
        if (lvl == null) lvl = manager.createLevel(lvlName);
        lvl.setGameMode(Constants.SHIP);
        lvl.setSpeed(Constants.SPEED_HALF);
        lvl.deleteObjects();

        double spacing = 0.0333;
        double mult = 1;
        double sampleX = 0;
        for (int i = 0; i < 200; i++) {
            double n = (noise.eval(sampleX + 1, 0)+1) * 97 + 53;

            int center = (int)n;
            GDObject newObj = lvl.addObject(8, i*30 + 300, center + 38);
            newObj.setRotation(180);
            newObj.setNoGlow();

            newObj = lvl.addObject(8, i*30 + 300, center - 38);
            newObj.setNoGlow();

            if (i % 50 == 0 && i != 0) {
                mult += 0.9;
                lvl.addObject(GDObject.createText(i*30 + 300, 200, "Diff up"));
            }
            sampleX += spacing*mult;
        }

        manager.save();
    }
}
