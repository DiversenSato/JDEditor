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
import java.util.zip.GZIPInputStream;

import static diversanto.gdmanager.Base64Functions.*;

public class GDManager {
    private String levelsData;
    private GDLevel[] levels;

    public GDManager(String levelDataPath) throws IOException {
        byte[] bytes;
        try {
            Path datPath = Paths.get(levelDataPath);
            bytes = Files.readAllBytes(datPath);
        } catch (IOException ignored) {
            throw new IllegalArgumentException("An error occurred while reading the data file. Maybe double check that the path is written correctly.");
        }

        //STEP 1, DECRYPT WITH XOR
        //Every byte needs to be XOR'ed with 11.
        //Also, levels are encoded using Base64URL so that needs converting to Base64.
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)(bytes[i] ^ 11);  //XOR's
        }
        URLToBase64(bytes);//Convert from Base64URL to Base64


        //STEP 2, DECODE WITH BASE64
        byte[] decodedBytes = base64URLDecode(bytes);


        //STEP 3, DECOMPRESS AS GZIP FILE
        levelsData = decompress(decodedBytes);



        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(levelsData)));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public static String decompress(byte[] bytes) throws IOException {
        System.out.println(bytes.length);
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));

        StringBuilder outStr = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            outStr.append(line);
        }
        return outStr.toString();
    }
}
