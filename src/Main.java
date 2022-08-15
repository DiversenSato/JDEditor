import diversanto.gdmanager.Color;
import diversanto.gdmanager.GDManager;
import diversanto.gdmanager.NoSuchLevelException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.zip.DataFormatException;

public class Main {
    public static void main(String[] args) throws Exception {
        GDManager gdManager = new GDManager("C:/Users/diver/AppData/Local/GeometryDash/CCLocalLevels.dat");

        System.out.println(gdManager.getLevel("level name").colors.get(0).storageFormat());
    }
    public static void copy(String theString) {
        StringSelection selection = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
