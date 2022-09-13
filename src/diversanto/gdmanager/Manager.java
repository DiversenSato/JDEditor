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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.zip.*;

import static diversanto.gdmanager.Base64Functions.isBase64Char;

public class Manager {
    public static boolean debug = true;

    private final ArrayList<Level> levels = new ArrayList<>();
    private final String basePath = System.getenv("APPDATA").replace("Roaming", "Local\\GeometryDash");

    public Manager() throws Exception {
        File levelDataFile = new File(basePath + "\\CCLocalLevels.dat");

        FileInputStream lin = new FileInputStream(levelDataFile);
        byte[] rawFile = lin.readAllBytes();   //<-- Contains all the bytes from CCLocalLevels.dat
        lin.close();

        //Create backup
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            String date = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss"));
            FileOutputStream backupStream = new FileOutputStream(basePath + "\\backups\\" + date + ".dat");
            backupStream.write(rawFile);   //Writes the save file to a backup file
            backupStream.close();
        } catch (IOException e) {
            System.err.println("An error occurred while attempting to create the backup");
            e.printStackTrace();
        }

        String lvlData = new String(rawFile, StandardCharsets.UTF_8);
        boolean isEncoded = !lvlData.startsWith("<");  //If the first byte of LocalLevels.dat starts with "<", it is not encoded
        if (isEncoded) {
            //
            // STEP 1: XOR WITH 11
            //
            System.out.println("Decoding " + rawFile.length + " bytes");
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

        //copy(lvlData);

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document;
        try {
            document = builder.parse(new InputSource(new StringReader(lvlData)));
        } catch (Exception e) {
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

    public void deleteAllLevels() {
        System.out.println("Deleted " + levels.size() + " level" + (levels.size() == 1 ? "" : "s"));
        levels.clear();
    }

    /**
     * Creates a new empty level from scratch.
     * @param levelName The name of the new level.
     * @return the new level.
     */
    public Level createLevel(String levelName) {
        Level newLevel = new Level(levelName);
        levels.add(0, newLevel);
        return newLevel;
    }

    /**
     * Saves all changes to CCLocalLevels.dat.
     * Not all properties have been parsed yet, so loading and saving a level might remove some data.
     * @throws IOException if there was an I/O error regarding writing to CCLocalLevels.
     */
    public void save() throws IOException {
        FileOutputStream saveOut = new FileOutputStream(basePath + "\\CCLocalLevels.dat");
        FileOutputStream backupOut = new FileOutputStream(basePath + "\\CCLocalLevels2.dat");
        byte[] save = constructSaveFile(true);
        System.out.println("Saving " + save.length + " bytes");
        saveOut.write(save);
        backupOut.write(save);
        saveOut.close();
        backupOut.close();
    }

    public byte[] constructSaveFile() {
        StringBuilder saveFile = new StringBuilder();
        saveFile.append("<?xml version=\"1.0\"?><plist version=\"1.0\" gjver=\"2.0\"><dict><k>LLM_01</k><d><k>_isArr</k><t />");

        for (int i = 0; i < levels.size(); i++) {
            String lvlEntry = String.format("<k>k_%d</k><d>%s</d>", i, levels.get(i));
            saveFile.append(lvlEntry);
        }
        saveFile.append("</d><k>LLM_02</k><i>35</i></dict></plist>");

        return saveFile.toString().getBytes(StandardCharsets.UTF_8);
    }
    public byte[] constructSaveFile(boolean encode) throws IOException {
        if (encode) {
            return xor(Base64Functions.encode(compress(constructSaveFile())).getBytes(StandardCharsets.UTF_8));
        } else {
            return constructSaveFile();
        }
    }



    /**
     * Unzips a byte array and returns a new array.
     * @param zippedBytes the zipped byte array.
     * @return the unzipped byte array.
     * @throws IOException if an I/O error has occurred.
     */
    public static String decompress(byte[] zippedBytes) throws IOException {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(zippedBytes));
        return new String(gis.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static byte[] compress(byte[] input) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        GZIPOutputStream gout = new GZIPOutputStream(byteStream);
        gout.write(input);
        gout.close();
        return byteStream.toByteArray();
    }

    /**
     * This function takes in an array of bytes, and individually XOR's every byte with the key, 11.
     * @param bytes The byte array to be XOR'ed.
     * @return the XOR'ed array.
     */
    public static byte[] xor(byte[] bytes) {
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

        int bytesRemoved = bytes.length - bOut.size();
        if (bytesRemoved > 0) System.out.println("Sanitation removed " + bytesRemoved + " bytes!");
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
}
