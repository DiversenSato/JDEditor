package diversanto.gdmanager;

import java.util.Base64;

public class Base64Functions {
    public static byte[] decode(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    public static byte[] URLToBase64(byte[] encoded) {
        for (int i = 0; i < encoded.length; i++) {
            if (encoded[i] == 0x2D) encoded[i] = 0x2B;
            if (encoded[i] == 0x5F) encoded[i] = 0x2F;
        }

        return encoded;
    }
}
