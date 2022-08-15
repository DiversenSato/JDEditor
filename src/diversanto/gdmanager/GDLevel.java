package diversanto.gdmanager;

import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.zip.DataFormatException;

import static diversanto.gdmanager.Base64Functions.base64URLDecode;
import static diversanto.gdmanager.GDManager.decompress;

public class GDLevel {
    protected String name;
    protected String description;
    public String data = "";
    public ArrayList<Color> colors = new ArrayList<>();

    protected GDLevel(NodeList dataList) throws Exception {
        int k = 0;
        while (!(dataList.item(k).getNodeName().equals("k") && dataList.item(k).getTextContent().equals("k4"))) {
            k++;
            if (k >= dataList.getLength()) return;
        }

        String lvlDat = dataList.item(k+1).getTextContent().replace("-", "+").replace("_", "/");
        String lvlData = decompress(Base64.getDecoder().decode(lvlDat.getBytes(StandardCharsets.UTF_8)));
        String[] lvlDataKeyValue = lvlData.split(",");

        for (int i = 0; i < lvlDataKeyValue.length/2; i++) {
            String key = lvlDataKeyValue[i*2];
            String value = lvlDataKeyValue[i*2+1];

            switch (key) {
                case "k2":
                    name = value;
                    break;
                case "k3":
                    description = new String(base64URLDecode(value), StandardCharsets.UTF_8);
                    break;
                case "k4":
                    data = decompress(base64URLDecode(value));
                    //Convert data to key value pairs
                    String[] split = data.split(",");
                    for (int j = 0; j < split.length / 2; j++) {
                        String dataKey = split[i*2];
                        String dataValue = split[i*2+1];

                        if (dataKey.equals("kS38")) {
                            String[] cols = dataValue.split("|");
                            for (String col : cols) {
                                colors.add(new Color(col));
                            }
                        }
                    }
                    break;
            }
        }
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
