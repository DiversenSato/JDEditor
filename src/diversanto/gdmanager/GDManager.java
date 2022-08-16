package diversanto.gdmanager;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.zip.*;

import static diversanto.gdmanager.Base64Functions.*;

public class GDManager {
    private GDLevel[] levels;

    public GDManager(String basePath) throws Exception {
        //
        // STEP 1: XOR WITH 11
        //
        File xorFile = new File(basePath + "LocalLevelsXOR.dat");
        xorFile.createNewFile();
        File levelDataFile = new File(basePath + "CCLocalLevels.dat");
        levelDataFile.createNewFile();
        File baseOutFile = new File(basePath + "LocalLevelsDecoded.dat");
        baseOutFile.createNewFile();
        File dOutFile = new File(basePath + "LocalLevelsDecompressed.dat");
        dOutFile.createNewFile();

        FileInputStream lin = new FileInputStream(levelDataFile);
        OutputStream xout = new FileOutputStream(xorFile);
        int i = 0;
        while((i = lin.read()) != -1) {
            int xored = i ^ 11;
            if (xored == 0) continue;
            if (xored == 45) xored = 43;
            if (xored == 95) xored = 47;

            xout.write(xored);
        }
        lin.close();
        xout.close();



        //
        // STEP 2: Base64 DECODE
        //
        FileInputStream xorin = new FileInputStream(xorFile);
        FileOutputStream baseOut = new FileOutputStream(baseOutFile);
        baseOut.write(Base64.getDecoder().decode(xorin.readAllBytes()));
        baseOut.close();
        xorin.close();



        //
        // STEP 3: DECOMPRESS GZIP
        //
        String lvlData = decompress(new FileInputStream(baseOutFile).readAllBytes());



        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(lvlData)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Element plist = document.getDocumentElement();
        Node dict = plist.getElementsByTagName("dict").item(0);

        int levelCount = (dict.getChildNodes().item(1).getChildNodes().getLength() - 2) / 2;
        levels = new GDLevel[levelCount];
        for (i = 0; i < levelCount; i++) {
            Node dataNode = dict.getChildNodes().item(1).getChildNodes().item(i*2+3);

            levels[i] = new GDLevel(dataNode.getChildNodes());
        }
    }

    public GDLevel getLevel(String levelName) throws NoSuchLevelException {
        for (GDLevel level : levels) {
            if (level.name.equalsIgnoreCase(levelName)) {
                return level;
            }
        }

        throw new NoSuchLevelException("There are no levels matching the name \"" + levelName + "\"!");
    }



    public static void copyString(String theString) {
        StringSelection selection = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }


    public static String decompress(byte[] str) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

        String outStr = "";
        String line;
        while ((line = bf.readLine()) != null) {
            outStr += line;
        }

        return outStr;
    }
}
