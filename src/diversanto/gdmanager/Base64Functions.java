package diversanto.gdmanager;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Functions {
    public static byte[] decode(byte[] bytes) {
        byte[] sanitized = Manager.sanitize(bytes);
        return Base64.getDecoder().decode(sanitized);
    }

    public static String encode(String str) {
        ByteArrayOutputStream arrOut = new ByteArrayOutputStream();
        for (Byte b: Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8))) {
            if (b == 43) b = 45;
            if (b == 47) b = 95;
            arrOut.write(b);
        }

        return arrOut.toString(StandardCharsets.UTF_8);
    }

    /**
     * Tests if the specified byte belongs to the Base64 schema.
     * @param b the UTF-8 character
     * @return true if the character belongs to Base64. Otherwise false
     */
    public static boolean isBase64Char(byte b) {
        return (b >= 65 && b <= 90) || (b >= 97 && b <= 122) || (b >= 48 && b <= 57) || b == 43 || b == 47 || b == 61;
    }
}
