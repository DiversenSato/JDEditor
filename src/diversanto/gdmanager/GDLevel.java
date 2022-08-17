package diversanto.gdmanager;

import diversanto.gdmanager.color.Color;
import org.w3c.dom.NodeList;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static diversanto.gdmanager.GDManager.decompress;

public class GDLevel extends GDConstants {
    protected String name;
    protected String description;
    protected String levelAuthor;
    protected int officialSongID = 0;

    protected int gameMode = GM_CUBE;
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



    protected GDLevel(NodeList dataList) throws Exception {
        for (int i = 0; i < dataList.getLength(); i++) {
            if (dataList.item(i).getNodeName().equals("k")) {
                String key = dataList.item(i++).getTextContent();
                String value = dataList.item(i).getTextContent();

                switch (key) {
                    case "k2" -> name = value;
                    case "k3" -> description = new String(Base64Functions.decode(value.getBytes()), StandardCharsets.UTF_8);
                    case "k4" -> {
                        String data = value;
                        if (!value.startsWith("kS38")) {
                            System.out.println("Decoding " + value.length() + " bytes for level data!");
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

                        System.out.println("Level constructed from the data: " + data);
                    }
                    case "k5" -> levelAuthor = value;
                    case "k8" -> officialSongID = Integer.parseInt(value);
                }
            }
        }
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
        return newObj;
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

    public String storageFormat() {
        StringBuilder formatted = new StringBuilder();

        formatted.append(String.format("<k>k2</k><s>%s</s>", name));
        formatted.append(String.format("<k>k4</k><s>%s</s>", formatData()));

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
}
