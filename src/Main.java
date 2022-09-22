import diversanto.gdmanager.Level;
import diversanto.gdmanager.Manager;

public class Main {
    public static void main(String[] args) throws Exception {
        Manager manager = new Manager();

        Level imageTest = manager.getLevel("Image test", true);
        imageTest.deleteObjects();
        imageTest.createImage("C:\\Users\\diver\\Pictures\\magnus.png", 150, 0);

        manager.save();
    }
}
