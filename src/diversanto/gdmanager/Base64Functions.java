package diversanto.gdmanager;

import java.util.Base64;

public class Base64Functions {
    public static byte[] base64URLDecode(byte[] bytes) {
        return Base64.getDecoder().decode(URLToBase64(trimBase64URL(bytes)));
    }

    public static byte[] base64URLDecode(String encoded) {
        return base64URLDecode(URLToBase64(encoded).getBytes());
    }

    public static String URLToBase64(String encoded) {
        return encoded.replace("-", "+").replace("_", "/");
    }

    public static byte[] URLToBase64(byte[] encoded) {
        for (int i = 0; i < encoded.length; i++) {
            if (encoded[i] == 0x2D) encoded[i] = 0x2B;
            if (encoded[i] == 0x5F) encoded[i] = 0x2F;
        }

        return encoded;
    }

    public static byte[] trimBase64URL(byte[] bytes) {
        int negativeOffset = 0;
        while (bytes[bytes.length-1 - negativeOffset] > 64) {
            negativeOffset++;
            if (negativeOffset >= 5) break;
        }

        byte[] returned = new byte[bytes.length - negativeOffset];
        if (bytes.length - negativeOffset >= 0) System.arraycopy(bytes, 0, returned, 0, bytes.length - negativeOffset);

        return returned;
    }

    public static byte[] trimBase64URL(String encoded) {
        return trimBase64URL(encoded.getBytes());
    }
}
