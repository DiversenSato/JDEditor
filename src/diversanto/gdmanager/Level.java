package diversanto.gdmanager;

import diversanto.gdmanager.color.Color;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static diversanto.gdmanager.Manager.decompress;

public class Level extends Constants {
    protected String name = "Template level";
    protected String description = "Created using JDEditor";
    protected String levelAuthor = "JDEditor";
    protected int officialSongID = 0;
    protected int version = -1;
    protected int binaryVersion = 35;
    protected int songID = -1;
    protected int originalLevelID = -1;
    protected int secondsSpent = -1;
    protected float worldX = 0;
    protected float worldY = 0;
    protected float zoom = 0;



    protected int gameMode = CUBE;
    protected boolean mini = false;
    protected int speed = SPEED_1X;
    protected int backgroundType = 0;
    protected int groundType = 0;
    protected boolean dual = false;
    protected boolean twoPlayer = false;
    protected int lineType = 0;
    protected int font = 0;

    protected boolean k13 = false;
    protected int k21 = 2;
    protected boolean k47 = false;
    protected int k81 = -1;
    protected String kI6 = null;



    private final ArrayList<Color> colors = new ArrayList<>();
    private final ArrayList<GDObject> objects = new ArrayList<>();
    private boolean hasData = false;

    private String extraData = "";

    protected Level(NodeList dataList, boolean isEncoded) throws Exception {
        int rawByteLength = -1;

        for (int i = 0; i < dataList.getLength(); i += 2) {
            if (dataList.item(i).getNodeName().equals("k")) {
                String key = dataList.item(i).getTextContent();
                String value = dataList.item(i+1).getTextContent();

                switch (key) {
                    case "kCEK" -> {}
                    case "k2" -> {
                        name = value;
                    }
                    case "k3" -> {
                        if (isEncoded) description = new String(Base64Functions.decode(value.getBytes()), StandardCharsets.UTF_8);
                        else description = value;
                    }
                    case "k4" -> {
                        String data = value;
                        if (!value.startsWith("kS38")) {
                            rawByteLength = value.length();
                            byte[] decoded = Base64Functions.decode(value.getBytes(StandardCharsets.UTF_8));
                            data = decompress(decoded);
                        }

                        //Convert data to key value pairs
                        String[] split = data.split(";");
                        String[] levelSettings = split[0].split(",");
                        for (int j = 0; j < levelSettings.length / 2; j++) {
                            String dataKey = levelSettings[j * 2];
                            String dataValue = levelSettings[j * 2 + 1];

                            switch (dataKey) {
                                case "kS38" -> {
                                    String[] cols = dataValue.split("\\|");
                                    for (String col : cols) {
                                        colors.add(new Color(col));
                                    }
                                }
                                case "kA2" -> gameMode = Math.min(Math.max(Integer.parseInt(dataValue), 0), 6);
                                case "kA3" -> mini = Integer.parseInt(dataValue) == 1;
                                case "kA4" -> speed = Math.min(Math.max(Integer.parseInt(dataValue), 0), 4);
                                case "kA6" -> backgroundType = Math.min(Math.max(Integer.parseInt(dataValue), 1), 20);
                                case "kA7" -> groundType = Math.min(Math.max(Integer.parseInt(dataValue), 1), 7);
                                case "kA8" -> dual = Integer.parseInt(dataValue) == 1;
                                case "kA10" -> twoPlayer = Integer.parseInt(dataValue) == 1;
                                case "kA17" -> lineType = Math.min(Math.max(Integer.parseInt(dataValue), 1), 2);
                                case "kA18" -> font = Math.min(Math.max(Integer.parseInt(dataValue), 0), 11);
                            }
                        }

                        //Add objects to this level
                        for (int j = 1; j < split.length; j++) {
                            objects.add(new GDObject(split[j]));
                        }

                        hasData = true;
                    }
                    case "k5" -> levelAuthor = value;
                    case "k8" -> officialSongID = Integer.parseInt(value);
                    case "k13" -> k13 = true;
                    case "k16" -> version = Integer.parseInt(value);
                    case "k21" -> k21 = Integer.parseInt(value);
                    case "k42" -> originalLevelID = Integer.parseInt(value);
                    case "k45" -> songID = Integer.parseInt(value);
                    case "k47" -> k47 = true;
                    case "k50" -> binaryVersion = Integer.parseInt(value);
                    case "k80" -> secondsSpent = Integer.parseInt(value);
                    case "k81" -> k81 = Integer.parseInt(value);
                    case "kI1" -> worldX = Float.parseFloat(value);
                    case "kI2" -> worldY = Float.parseFloat(value);
                    case "kI3" -> zoom = Float.parseFloat(value);
                    case "kI6" -> {
                        kI6 = elementToString((Element)dataList.item(i+1));
                    }
                    default -> {
                        System.out.println("Unserialized key: " + key);
                        extraData += elementToString((Element)dataList.item(i));
                        extraData += elementToString((Element)dataList.item(i+1));
                    }
                }
            }
        }

        System.out.println("Loaded \"" + name + "\" from " + rawByteLength + " bytes");
    }

    protected Level(String name) {
        this.name = name;
        colors.add(new Color("1_40_2_125_3_255_11_255_12_255_13_255_6_1000_7_1_15_1_18_0_8_1"));
        colors.add(new Color("1_0_2_102_3_255_11_255_12_255_13_255_6_1001_7_1_15_1_18_0_8_1"));
        colors.add(new Color("1_0_2_102_3_255_11_255_12_255_13_255_6_1009_7_1_15_1_18_0_8_1"));
        colors.add(new Color("1_255_2_255_3_255_11_255_12_255_13_255_6_1002_5_1_7_1_15_1_18_0_8_1"));
        colors.add(new Color("1_255_2_255_3_255_11_255_12_255_13_255_6_1005_5_1_7_1_15_1_18_0_8_1"));
        colors.add(new Color("1_255_2_185_3_0_11_255_12_255_13_255_6_1006_5_1_7_1_15_1_18_0_8_1"));

        extraData += "<k>k21</k><i>2</i>";
        extraData += "<k>k50</k><i>35</i>";
        extraData += "<k>k47</k><t/>";
    }

    /**
     * Finds the color that uses the specified channel ID and returns it.
     * @param colorChannel The color channel ID
     * @return The color
     */
    public Color getColor(int colorChannel) {
        for (Color color : colors) {
            if (color.getChannel() == colorChannel) return color;
        }

        return null;
    }

    /**
     * Adds a new empty object to this level. The ID, x and y values can be specified.
     * @param id The object ID
     * @param x The x position
     * @param y The y position
     * @return The newly created GDObject
     */
    public GDObject addObject(int id, float x, int y) {
        GDObject newObj = new GDObject(id, x, y);
        objects.add(newObj);
        hasData = true;
        return newObj;
    }

    /**
     * Adds a GDObject to this level.
     * @param o The new object
     */
    public void addObject(GDObject o) {
        objects.add(o);
        hasData = true;
    }

    /**
     * Returns the position of the last object in this level. Use Point2D.x or Point2D.y to get their respective values.
     * @return The last position
     */
    public Point2D.Float getLastObjectPosition() {
        if (objects.size() > 0) {
            GDObject last = objects.get(0);

            for (GDObject o : objects) {
                if (o.getPosition().x > last.getPosition().x) last = o;
            }

            return last.getPosition();
        } else {
            return new Point2D.Float(15, 15);
        }
    }

    /**
     * returns the name of this level
     * @return The level name
     */
    public String getName() {
        if (name == null) {
            name = "Level name";
        }

        return name;
    }

    /**
     * Sets the description of this level.
     * @param desc The new description
     */
    public void setDescription(String desc) {
        description = desc;
    }

    /**
     * Simply returns the description of this level.
     * @return The description
     */
    public String getDescription() {
        if (description == null) {
            description = "Description";
        }

        return description;
    }

    /**
     * Sets the starting game mode of this level.
     * Please note that gdmanager.Constants contains constants representing the different game modes.
     * @param gameMode The game mode
     */
    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Sets the starting speed for this level.
     * Please note that gdmanager.Constants contains constants representing the different speeds. This is because the numbering order is a bit weird, so please use those.
     * @param speed The speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Adds the given color channel to this level.
     * There currently is no limit to the channel ID and I don't intend finding out what happens when you go over 1009
     * @param color The color object
     */
    public void addColorChannel(Color color) {
        colors.add(color);
    }

    /**
     * Scans the used group IDs and finds a free group.
     * @return The group ID
     */
    public int nextFreeGroup() {
        boolean foundMatch = false;
        int i = 1;
        while(i < 999 && !foundMatch) {  //Loop through every possible ID
            foundMatch = true;

            //If any objects has this id, skip
            for (GDObject o : objects) {
                if (o.hasGroupID(i)) {
                    foundMatch = false;

                    i++;
                    break;
                }
            }
        }

        return i;
    }

    /**
     * Removes all objects containing the given group ID.
     * @param g The group ID
     */
    public void deleteGroup(int g) {
        for (int i = objects.size()-1; i >= 0; i--) {
            if (objects.get(i).hasGroupID(g)) objects.remove(i);
        }
    }

    /**
     * Scans the color channels and finds a free id.
     * @return The color channel ID
     */
    public int nextFreeColorChannel() {
        boolean foundMatch = false;
        int i = 1;
        while(i < 999 && !foundMatch) {  //Loop through every possible ID
            foundMatch = true;

            //If any objects has this id, skip
            for (Color c : colors) {
                if (c.getChannel() == i) {
                    foundMatch = false;

                    i++;
                    break;
                }
            }
        }

        return i;
    }

    /**
     * Converts this level to something that goes in the save file.
     * This is used internally to save levels, so you don't have to worry about this method.
     * I can't stop you though ;)
     * @return The string representation of this level
     */
    @Override
    public String toString() {
        //Fix object density
        int objectCount = getObjectCount();
        float minLastX = (float)objectCount / 115f * 30f;  //115 is the amount of objects per block. This may differ; I have no idea how this works other than it won't start the level with this.
        if (minLastX > getLastObjectPosition().x) {  //Also, the actual value is closer to 115,843.
            addObject(1, minLastX, 2100);
            System.out.println(name + " was expanded to fix object density errors at x = " + minLastX);
        }



        StringBuilder formatted = new StringBuilder();

        formatted.append("<k>kCEK</k><i>4</i>");
        formatted.append(String.format("<k>k2</k><s>%s</s>", name));
        formatted.append(String.format("<k>k3</k><s>%s</s>", Base64Functions.encode(description)));
        if (hasData) formatted.append(String.format("<k>k4</k><s>%s</s>", formatData()));
        formatted.append(String.format("<k>k5</k><s>%s</s>", levelAuthor));
        formatted.append("<k>k13</k><t/>");
        formatted.append("<k>k21</k><i>").append(k21).append("</i>");
        if (version != -1) formatted.append("<k>k16</k><i>").append(version).append("</i>");
        if (secondsSpent != -1) formatted.append("<k>k80</k><i>" +  secondsSpent + "</i>");
        if (k81 != -1) formatted.append("<k>k81</k><i>").append(k81).append("</i>");
        if (originalLevelID != -1) formatted.append("<k>k42</k><i>" + originalLevelID + "</i>");
        if (songID != -1) formatted.append("<k>k45</k><i>" + songID + "</i>");
        if (k47) formatted.append("<k>k47</k><t />");
        formatted.append("<k>k50</k><i>").append(binaryVersion).append("</i>");
        formatted.append(extraData);

        formatted.append("<k>kI1</k><r>").append(worldX).append("</r>");
        formatted.append("<k>kI2</k><r>").append(worldY).append("</r>");
        formatted.append("<k>kI3</k><r>").append(zoom).append("</r>");
        formatted.append("<k>kI6</k>");
        if (kI6 != null) formatted.append(kI6);
        else formatted.append("<d />");

        return formatted.toString();
    }

    private String formatData() {
        StringBuilder formatted = new StringBuilder();

        formatted.append("kS38,");
        for (Color color : colors) {
            formatted.append(color).append("|");
        }

        formatted.append(",kA13,0,kA15,0,kA16,0,kA14,,");
        formatted.append(String.format("kA6,%d,", backgroundType));
        formatted.append(String.format("kA7,%d,", groundType));
        formatted.append(String.format("kA17,%d,", lineType));
        formatted.append(String.format("kA18,%d,", font));
        formatted.append("kS39,0,");
        formatted.append(String.format("kA2,%d,", gameMode));
        formatted.append(String.format("kA3,%d,", mini ? 1 : 0));
        formatted.append(String.format("kA8,%d,", dual ? 1 : 0));
        formatted.append(String.format("kA4,%d,", speed));
        formatted.append("kA9,0,");
        formatted.append(String.format("kA10,%d,", twoPlayer ? 1 : 0));
        formatted.append("kA11,0;");

        for (GDObject object : objects) {
            formatted.append(object);
        }

        try {
            return Base64Functions.encode(Manager.compress(formatted.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes all objects from this level.
     */
    public void deleteObjects() {
        objects.clear();
    }

    /**
     * Resets the given level so all it's data is removed. This includes gamemodes, level data, name and so on.
     * @param lvl The level to be resat
     */
    public static void reset(Level lvl) {
        lvl = new Level(lvl.name);
    }

    public static String elementToString(Element n) {
        StringBuilder out = new StringBuilder();

        int childLength = n.getChildNodes().getLength();
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            String nodeName = n.getNodeName();
            out.append("<").append(nodeName).append(">");

            for (int i = 0; i < n.getChildNodes().getLength(); i++) {
                Node child = n.getChildNodes().item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) out.append(elementToString((Element)child));
                else if (child.getNodeType() == Node.TEXT_NODE) out.append(child.getTextContent());
            }

            out.append("</").append(nodeName).append(">");
        } else if (n.getNodeType() == Node.TEXT_NODE) {
            return n.getTextContent();
        }

        return out.toString();
    }

    /**
     * Counts the amount of objects in this level.
     * @return The object count
     */
    public int getObjectCount() {
        return objects.size();
    }



    /**
     * Will add a picture to this level.
     * The picture, of course, is very pixelated, as increasing resolution exponentially increases object count.
     * @param imagePath The path to the image file
     * @param xPosition X position of the bottom left corner
     * @param yPosition Y position of the bottom left corner
     */
    public void createImage(String imagePath, int xPosition, int yPosition) {
        BufferedImage picture = null;

        try {
            picture = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("An error occurred while reading the image data. Does the specified path exist or is correctly spelled?");
            e.printStackTrace();
            System.out.println("Execution will continue without adding an image!");
        }

        Color col = new Color(nextFreeColorChannel());
        col.setRGB(255, 0, 0);
        addColorChannel(col);

        assert picture != null;
        int maxResolution = 50;
        int pixelSize = 3;
        float scale = (float)pixelSize / 30f;

        int minResolution = maxResolution * picture.getHeight() / picture.getWidth();
        int xRes = (picture.getHeight() < picture.getWidth()) ? maxResolution : minResolution;
        int yRes = (picture.getHeight() > picture.getWidth()) ? maxResolution : minResolution;
        int sampleDistance = picture.getWidth() / xRes;
        int pictureGroup = nextFreeGroup();

        for (int y = 0; y < yRes; y++) {
            for (int x = 0; x < xRes; x++) {
                GDObject obj = new GDObject(211, x * pixelSize + xPosition + ((int)(scale/2f)), y * -pixelSize + yRes*3 + yPosition + ((int)(scale/2f)));
                obj.setScale(scale);
                obj.addGroup(pictureGroup);
                obj.setColorChannel(col);
                obj.setEditorLayer(101);

                obj.getHSB().setRGB(picture.getRGB(x * sampleDistance, y * sampleDistance));

                addObject(obj);
            }
        }

        setCameraPos(xPosition + (int)(xRes*scale / 2 * 30), yPosition + (int)(yRes*scale / 2 * 30));
    }

    /**
     * Adjusts the camera position to look at a certain coordinate.
     * This method is NOT 100% accurate, so don't go using this as a way to mark positions if accuracy is needed.
     * @param x Camera x position
     * @param y Camera y position
     */
    public void setCameraPos(int x, int y) {
        worldX = (int)((-x + 285) * zoom);
        worldY = (int)((-y + 106) * zoom);
    }
}
