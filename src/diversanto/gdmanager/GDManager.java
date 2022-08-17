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
import java.util.ArrayList;
import java.util.zip.*;

public class GDManager {
    private final ArrayList<GDLevel> levels = new ArrayList<>();
    private String basePath = "";

    public GDManager(String basePath) throws Exception {
        //
        // STEP 1: XOR WITH 11
        //
        this.basePath = basePath;
        File levelDataFile = new File(basePath);

        FileInputStream lin = new FileInputStream(levelDataFile);
        byte[] xoredData = xor(lin.readAllBytes());
        lin.close();



        //
        // STEP 2: Base64 DECODE
        //
        byte[] baseOut = Base64Functions.decode(xoredData);  //decode() also sanitizes



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
        for (int i = 0; i < levelCount; i++) {
            Node dataNode = dict.getChildNodes().item(1).getChildNodes().item(i*2+3);

            levels.add(new GDLevel(dataNode.getChildNodes()));
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

    public void deleteLevel(String levelName) {
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getName().equals(levelName)) {
                levels.remove(i);
                break;
            }
        }
    }



    public void save() throws IOException {
        StringBuilder saveFile = new StringBuilder();
        saveFile.append("<?xml version=\"1.0\"?><plist version=\"1.0\" gjver=\"2.0\"><dict><k>LLM_01</k><d><k>_isArr</k><t />");

        for (int i = 0; i < levels.size(); i++) {
            String lvlFormatted = levels.get(i).storageFormat();
            String lvlEntry = String.format("<k>k_%d</k><d><k>kCEK</k><i>4</i>%s<k>k50</k><i>35</i><k>k47</k><t /><k>kI1</k><r>0</r><k>kI2</k><r>36</r><k>kI3</k><r>1</r><k>kI6</k><d><k>0</k><s>0</s><k>1</k><s>0</s><k>2</k><s>0</s><k>3</k><s>0</s><k>4</k><s>0</s><k>5</k><s>0</s><k>6</k><s>0</s><k>7</k><s>0</s><k>8</k><s>0</s><k>9</k><s>0</s><k>10</k><s>0</s><k>11</k><s>0</s><k>12</k><s>0</s></d></d>", i, lvlFormatted);
            saveFile.append(lvlEntry);
        }
        saveFile.append("</d><k>LLM_02</k><i>35</i></dict></plist>");

        FileOutputStream saveOut = new FileOutputStream(basePath);
        copy(saveFile.toString());
        saveOut.write(saveFile.toString().getBytes(StandardCharsets.UTF_8));
        saveOut.close();
    }


    public static String decompress(byte[] str) throws Exception {
        System.out.println("Bytes to decompress: " + str.length);
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str));
        return new String(gis.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static byte[] xor(byte[] bytes) {
        System.out.println("Bytes to xor: " + bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)(bytes[i] ^ 11);
        }

        return bytes;
    }

    public static byte[] sanitize(byte[] bytes) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        for (byte aByte : bytes) {
            byte b = aByte;
            if (b == 45) b = 43;
            if (b == 95) b = 47;
            if (!isBase64Char(b)) continue;

            bOut.write(b);
        }

        System.out.println("Sanitation removed " + (bytes.length - bOut.size()) + " bytes!");
        return bOut.toByteArray();
    }

    private static boolean isBase64Char(byte b) {
        return (b >= 65 && b <= 90) || (b >= 97 && b <= 122) || (b >= 48 && b <= 57) || b == 43 || b == 47 || b == 61;
    }

    public static void copy(String theString) {
        StringSelection selection = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
