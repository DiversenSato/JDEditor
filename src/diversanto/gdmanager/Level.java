package diversanto.gdmanager;

import diversanto.gdmanager.color.Color;
import org.w3c.dom.NodeList;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static diversanto.gdmanager.Manager.copy;
import static diversanto.gdmanager.Manager.decompress;

public class Level extends Constants {
    protected String name = "Template level";
    protected String description = "Created using JDEditor";
    protected String levelAuthor = "JDEditor";
    protected int officialSongID = 0;
    protected int version = 1;
    protected int secondsSpent = 0;

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



    protected Level(NodeList dataList, boolean isEncoded) throws Exception {
        for (int i = 0; i < dataList.getLength(); i++) {
            if (dataList.item(i).getNodeName().equals("k")) {
                String key = dataList.item(i++).getTextContent();
                String value = dataList.item(i).getTextContent();

                switch (key) {
                    case "k2" -> name = value;
                    case "k3" -> {
                        if (isEncoded) description = new String(Base64Functions.decode(value.getBytes()), StandardCharsets.UTF_8);
                        else description = value;
                    }
                    case "k4" -> {
                        String data = value;
                        if (!value.startsWith("kS38")) {
                            System.out.println("Decoding " + value.length() + " bytes for level data!");
                            byte[] decoded = Base64Functions.decode(value.getBytes(StandardCharsets.UTF_8));
                            data = decompress(decoded);
                        }
                        copy(data);

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

                        hasData = true;
                    }
                    case "k5" -> levelAuthor = value;
                    case "k8" -> officialSongID = Integer.parseInt(value);
                    case "k16" -> version = Integer.parseInt(value);
                    case "k80" -> secondsSpent = Integer.parseInt(value);
                }
            }
        }
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

    protected String storageFormat() {
        StringBuilder formatted = new StringBuilder();

        formatted.append(String.format("<k>k2</k><s>%s</s>", name));
        formatted.append(String.format("<k>k3</k><s>%s</s>", Base64Functions.encode(description)));
        if (hasData) formatted.append(String.format("<k>k4</k><s>%s</s>", formatData()));
        formatted.append(String.format("<k>k5</k><s>%s</s>", levelAuthor));
        formatted.append("<k>k13</k><t />");
        formatted.append("<k>k21</k><i>2</i>");
        formatted.append(String.format("<k>k16</k><i>%s</i>", version));
        if (hasData) formatted.append(String.format("<k>k80</k><i>%s</i>", secondsSpent));
        formatted.append("<k>k50</k><i>35</i>");
        formatted.append("<k>k47</k><t />");
        formatted.append("<k>kI1</k><r>0</r>");
        formatted.append("<k>kI2</k><r>36</r>");
        formatted.append("<k>kI3</k><r>1</r>");

        formatted.append("<k>kI6</k><d>");
        for (int i = 0; i < 12; i++) {
            formatted.append(String.format("<k>%d</k><s>0</s>", i));
        }
        formatted.append("</d>");

        return formatted.toString();
    }

    private String formatData() {
        StringBuilder formatted = new StringBuilder();

        formatted.append("kS38,");
        for (Color color : colors) {
            formatted.append(color.storageFormat()).append("|");
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
            formatted.append(object.storageFormat());
        }

        return formatted.toString();
    }

    public void deleteObjects() {
        objects.clear();
    }
}
