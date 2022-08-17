import diversanto.gdmanager.GDLevel;
import diversanto.gdmanager.GDManager;

public class Main {
    public static void main(String[] args) throws Exception {
        GDManager gdManager = new GDManager("C:/Users/diver/AppData/Local/GeometryDash/CCLocalLevels.dat");

        /*GDLevel lvl = gdManager.getLevel("level name");
        for (int i = 0; i < 700; i++) {
            lvl.addObject(i, 100 + i*300, 300);
        }*/
        gdManager.deleteLevel("level name");

        gdManager.save();
    }
}
