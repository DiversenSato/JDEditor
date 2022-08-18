import diversanto.gdmanager.GDLevel;
import diversanto.gdmanager.GDManager;
import diversanto.gdmanager.GDObject;

public class Main {
    public static void main(String[] args) throws Exception {
        GDManager gdManager = new GDManager();

        GDLevel lvl;
        if ((lvl = gdManager.getLevel("level name")) == null) {
            lvl = gdManager.createLevel("level name");
        } else {
            lvl.deleteObjects();
        }

        lvl.setDescription("Displays the id's of the first 1700 objects along with the object in the editor");

        for (int i = 0; i < 1700; i++) {
            lvl.addObject(i, 100 + i*90, 90);
            lvl.addObject(GDObject.createText(100 + i*90, 150, String.valueOf(i)));
        }

        gdManager.save();
    }
}
