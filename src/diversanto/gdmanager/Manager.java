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

import static diversanto.gdmanager.Base64Functions.isBase64Char;

public class Manager {
    private final ArrayList<Level> levels = new ArrayList<>();
    private String basePath = "";

    public Manager() throws Exception {
        this.basePath = System.getenv("APPDATA").replace("Roaming", "Local\\GeometryDash\\CCLocalLevels.dat");
        File levelDataFile = new File(basePath);

        FileInputStream lin = new FileInputStream(levelDataFile);
        byte[] rawFile = lin.readAllBytes();
        lin.close();

        String lvlData = new String(rawFile, StandardCharsets.UTF_8);
        boolean isEncoded = !lvlData.startsWith("<");  //If the first byte of the LocalLevels.dat starts with "<", it is not encoded
        if (isEncoded) {
            //
            // STEP 1: XOR WITH 11
            //
            byte[] xoredData = xor(rawFile);


            //
            // STEP 2: Base64 DECODE
            //
            byte[] baseOut = Base64Functions.decode(xoredData);  //decode() also sanitizes


            //
            // STEP 3: DECOMPRESS GZIP
            //
            lvlData = decompress(baseOut);
        }

        copy(lvlData);

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document;
        try {
            document = builder.parse(new InputSource(new StringReader(lvlData)));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        assert document != null;
        Element plist = document.getDocumentElement();
        Node dict = plist.getElementsByTagName("dict").item(0);
        Element LLM = (Element)dict.getFirstChild().getNextSibling();
        NodeList levelEntries = LLM.getElementsByTagName("k");
        for (int i = 0; i < levelEntries.getLength(); i++) {
            Element key = (Element)levelEntries.item(i);

            if (key.getTextContent().startsWith("k_")) {
                levels.add(new Level(key.getNextSibling().getChildNodes(), isEncoded));
            }
        }
    }

    /**
     * Fetches a level by searching for the name.
     * If the level does not exist, null will be returned.
     * @param levelName The name of the level.
     * @return the level.
     */
    public Level getLevel(String levelName) {
        for (Level level : levels) {
            if (level.name.equals(levelName)) {
                return level;
            }
        }

        return null;
    }

    /**
     * Deletes the specified level if the GDManager has detected a level with that name.
     * @param levelName The name of the level to be deleted.
     * @return false if no level was deleted. Otherwise true.
     */
    public boolean deleteLevel(String levelName) {
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getName().equals(levelName)) {
                levels.remove(i);

                return true;
            }
        }

        return false;
    }


    /**
     * Saves all changes to CCLocalLevels.dat.
     * Not all properties have been parsed yet, so loading and saving a level might remove some data.
     * @throws IOException if there was an I/O error regarding writing to CCLocalLevels.
     */
    public void save() throws IOException {
        FileOutputStream saveOut = new FileOutputStream(basePath);
        FileOutputStream backupOut = new FileOutputStream(basePath.replace(".dat", "2.dat"));
        saveOut.write(constructSaveFile().getBytes(StandardCharsets.UTF_8));
        backupOut.write(constructSaveFile().getBytes(StandardCharsets.UTF_8));
        saveOut.close();
        backupOut.close();
    }

    private String constructSaveFile() {
        StringBuilder saveFile = new StringBuilder();
        saveFile.append("<?xml version=\"1.0\"?><plist version=\"1.0\" gjver=\"2.0\"><dict><k>LLM_01</k><d><k>_isArr</k><t />");

        for (int i = 0; i < levels.size(); i++) {
            String lvlFormatted = levels.get(i).storageFormat();
            String lvlEntry = String.format("<k>k_%d</k><d><k>kCEK</k><i>4</i>%s</d>", i, lvlFormatted);
            saveFile.append(lvlEntry);
        }
        saveFile.append("</d><k>LLM_02</k><i>35</i></dict></plist>");

        return saveFile.toString();
    }


    /**
     * Unzips a byte array and returns a new array.
     * @param zippedBytes the zipped byte array.
     * @return the unzipped byte array.
     * @throws IOException if an I/O error has occurred.
     */
    public static String decompress(byte[] zippedBytes) throws IOException {
        System.out.println("Bytes to decompress: " + zippedBytes.length);
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(zippedBytes));
        return new String(gis.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * This function takes in an array of bytes, and individually XOR's every byte with the key, 11.
     * @param bytes The byte array to be XOR'ed.
     * @return the XOR'ed array.
     */
    public static byte[] xor(byte[] bytes) {
        System.out.println("Bytes to xor: " + bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)(bytes[i] ^ 11);
        }

        return bytes;
    }

    /**
     * Takes in an array of bytes and prepares it for Base64 decoding.
     * This includes converting Base64URL characters to Base64 characters, and filtering out non-Base64 characters.
     * @param bytes the array to be sanitized.
     * @return the sanitized array.
     */
    public static byte[] sanitize(byte[] bytes) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        for (byte aByte : bytes) {
            byte b = aByte;
            if (b == 45) b = 43;
            if (b == 95) b = 47;
            if (!isBase64Char(b)) break;

            bOut.write(b);
        }

        System.out.println("Sanitation removed " + (bytes.length - bOut.size()) + " bytes!");
        return bOut.toByteArray();
    }

    /**
     * Simply copies a string to the clipboard.
     * @param theString to be copied.
     */
    public static void copy(String theString) {
        StringSelection selection = new StringSelection(theString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    /**
     * Creates a new empty level from scratch.
     * @param levelName The name of the new level.
     * @return the new level.
     */
    public Level createLevel(String levelName) {
        Level newLevel = new Level(levelName);
        levels.add(newLevel);
        return newLevel;
    }
}
