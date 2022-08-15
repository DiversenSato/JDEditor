package diversanto.gdmanager;

import org.w3c.dom.NodeList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;

import static diversanto.gdmanager.Base64Functions.base64URLDecode;
import static diversanto.gdmanager.GDManager.decompress;

public class GDLevel {
    protected String name;
    protected String description;
    public String data = "";

    protected GDLevel(NodeList dataList) throws IOException, DataFormatException {
        for (int i = 0; i < dataList.getLength()/2; i++) {
            String key = dataList.item(i*2).getTextContent();
            String value = dataList.item(i*2+1).getTextContent();

            switch (key) {
                case "k2":
                    name = value;
                    break;
                case "k3":
                    description = new String(base64URLDecode(value), StandardCharsets.UTF_8);
                    break;
                case "k4":
                    data = new String(decompress(base64URLDecode(value)), StandardCharsets.UTF_8);
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
