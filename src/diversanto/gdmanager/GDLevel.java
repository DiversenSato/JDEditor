package diversanto.gdmanager;

import org.w3c.dom.NodeList;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static diversanto.gdmanager.GDManager.decompress;

public class GDLevel extends GDConstants {
    protected String name;
    protected String description;
    public String data = "";
    public int gameMode = GM_CUBE;
    protected boolean mini = false;
    protected int speed = SPEED_1X;
    protected int backgroundType = 0;
    protected int groundType = 0;
    protected boolean dual = false;
    protected boolean twoPlayer = false;
    protected int lineType = 0;
    protected int font = 0;



    public ArrayList<Color> colors = new ArrayList<>();



    protected GDLevel(NodeList dataList) throws Exception {
        for (int i = 0; i < dataList.getLength(); i++) {
            if (dataList.item(i).getNodeName().equals("k")) {
                String key = dataList.item(i++).getTextContent();
                String value = dataList.item(i).getTextContent();

                switch (key) {
                    case "k2" -> name = value;
                    case "k3" -> description = new String(Base64Functions.decode(value.getBytes()), StandardCharsets.UTF_8);
                    case "k4" -> {
                        byte[] decoded = Base64Functions.decode(GDManager.sanitize(value.getBytes()));
                        data = decompress(decoded);

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
}
