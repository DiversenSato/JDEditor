package diversanto.gdmanager;

import java.util.Base64;

public class Base64Functions {
    public static byte[] decode(byte[] bytes) {
        return Base64.getDecoder().decode(GDManager.sanitize(bytes));
    }
}
