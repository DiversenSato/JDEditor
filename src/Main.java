import diversanto.gdmanager.GDManager;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Main {
    public static void main(String[] args) throws Exception {
        GDManager gdManager = new GDManager("C:/Users/diver/AppData/Local/GeometryDash/");

        System.out.println(gdManager.getLevel("level name").getColor(1000).getRed());
    }

    public static void copy(String theString) {
        StringSelection selection = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
