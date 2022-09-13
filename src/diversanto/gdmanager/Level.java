package diversanto.gdmanager;

import diversanto.gdmanager.color.Color;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    protected int songID = -1;
    protected int secondsSpent = -1;

    protected int gameMode = CUBE;
    protected boolean mini = false;
    protected int speed = SPEED_1X;
    protected int backgroundType = 0;
    protected int groundType = 0;
    protected boolean dual = false;
    protected boolean twoPlayer = false;
    protected int lineType = 0;
    protected int font = 0;



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
                    default -> {
                        extraData += elementToString((Element)dataList.item(i));
                        extraData += elementToString((Element)dataList.item(i+1));
                    }
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
                    case "k16" -> version = Integer.parseInt(value);
                    case "k45" -> songID = Integer.parseInt(value);
                    case "k80" -> secondsSpent = Integer.parseInt(value);
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
    }

    public Color getColor(int colorChannel) {
        for (Color color : colors) {
            if (color.getChannel() == colorChannel) return color;
        }

        return null;
    }

    public GDObject addObject(int id, int x, int y) {
        GDObject newObj = new GDObject(id, x, y);
        objects.add(newObj);
        hasData = true;
        return newObj;
    }

    public void addObject(GDObject o) {
        objects.add(o);
        hasData = true;
    }

    public String getName() {
        if (name == null) {
            name = "Level name";
        }

        return name;
    }

    public boolean setDescription(String desc) {
        if (desc.length() <= 140) {
            description = desc;
            return true;
        }

        return false;
    }

    public String getDescription() {
        if (description == null) {
            description = "Description";
        }

        return description;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void addColorChannel(Color channel) {
        colors.add(channel);
    }

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

    public void deleteGroup(int g) {
        for (int i = objects.size()-1; i >= 0; i--) {
            if (objects.get(i).hasGroupID(g)) objects.remove(i);
        }
    }

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

    @Override
    public String toString() {
        StringBuilder formatted = new StringBuilder();

        formatted.append(String.format("<k>k2</k><s>%s</s>", name));
        formatted.append(String.format("<k>k3</k><s>%s</s>", Base64Functions.encode(description)));
        if (hasData) formatted.append(String.format("<k>k4</k><s>%s</s>", formatData()));
        formatted.append(String.format("<k>k5</k><s>%s</s>", levelAuthor));
        if (songID != -1) formatted.append("<k>k45</k><i>" + songID + "</i>");
        if (version != -1) formatted.append(String.format("<k>k16</k><i>%s</i>", version));
        if (secondsSpent != -1) formatted.append("<k>k80</k><i>" +  secondsSpent + "</i>");
        formatted.append(extraData);

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

    public void deleteObjects() {
        objects.clear();
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
}
