import diversanto.gdmanager.Color;
import diversanto.gdmanager.GDManager;
import diversanto.gdmanager.NoSuchLevelException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchLevelException {
        GDManager gdManager = new GDManager("C:/Users/Daniel/AppData/Local/GeometryDash/CCLocalLevels.dat");
//
//        String data = gdManager.getLevel("level name").data;
//        System.out.println(data);
//        copy(data);
        Color col = new Color();
        System.out.println(col.storageFormat());
    }
    public static void copy(String theString) {
        StringSelection selection = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
