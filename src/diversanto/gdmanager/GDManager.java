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
import java.util.zip.*;

public class GDManager {
    private GDLevel[] levels;

    public GDManager(String basePath) throws Exception {
        //
        // STEP 1: XOR WITH 11
        //
        File levelDataFile = new File(basePath + "CCLocalLevels.dat");
        levelDataFile.createNewFile();

        FileInputStream lin = new FileInputStream(levelDataFile);
        byte[] xoredData = sanitize(xor(lin.readAllBytes()));
        lin.close();



        //
        // STEP 2: Base64 DECODE
        //
        byte[] baseOut = Base64Functions.decode(xoredData);



        //
        // STEP 3: DECOMPRESS GZIP
        //
        String lvlData = decompress(baseOut);



        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(lvlData)));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        assert document != null;
        Element plist = document.getDocumentElement();
        Node dict = plist.getElementsByTagName("dict").item(0);

        int levelCount = (dict.getChildNodes().item(1).getChildNodes().getLength() - 2) / 2;
        levels = new GDLevel[levelCount];
        for (int i = 0; i < levelCount; i++) {
            Node dataNode = dict.getChildNodes().item(1).getChildNodes().item(i*2+3);

            levels[i] = new GDLevel(dataNode.getChildNodes());
        }
    }

    public GDLevel getLevel(String levelName) throws NoSuchLevelException {
        for (GDLevel level : levels) {
            if (level.name.equals(levelName)) {
                return level;
            }
        }

        throw new NoSuchLevelException("There are no levels matching the name \"" + levelName + "\"! Maybe check capitalization and spelling.");
    }



    public static void copyString(String theString) {
        StringSelection selection = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }


    public static String decompress(byte[] str) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));

        StringBuilder outStr = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            outStr.append(line);
        }

        return outStr.toString();
    }

    public static byte[] xor(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)(bytes[i] ^ 11);
        }

        return bytes;
    }

    public static byte[] sanitize(byte[] bytes) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        for (byte aByte : bytes) {
            byte b = aByte;
            if (b == 0) continue;
            if (b == 45) b = 43;
            if (b == 95) b = 47;

            bOut.write(b);
        }

        return bOut.toByteArray();
    }
}
