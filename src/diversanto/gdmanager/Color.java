package diversanto.gdmanager;

public class Color {
    private int red = 40;
    private int green = 125;
    private int blue = 255;
    private int copyPlayerCol = 0;
    private boolean blending = false;
    private int colorChannel = 1000;
    private int opacity = 100;
    private int copyColorChannel = 1;
    private boolean useColorCopy = false;
    private HSB hsb = new HSB();

    public Color(String data) {
        String[] split = data.split("_");
        for (int i = 0; i < split.length / 2; i++) {
            int key = Integer.parseInt(split[i*2]);
            String value = split[i*2+1];

            switch (key) {
                case 1:
                    red = Integer.parseInt(value);
                    break;
                case 2:
                    green = Integer.parseInt(value);
                    break;
                case 3:
                    blue = Integer.parseInt(value);
                    break;
                case 4:
                    copyPlayerCol = Integer.parseInt(value);
                    break;
                case 5:
                    blending = Integer.parseInt(value) == 1;
                    break;
                case 6:
                    colorChannel = Integer.parseInt(value);
                    break;
                case 7:
                    opacity = deFormatFloat(value);
                    break;
                case 9:
                    useColorCopy = true;
                    copyColorChannel = Integer.parseInt(value);
                    break;
                case 10:
                    useColorCopy = true;
                    hsb = HSB.fromString(value);
                    break;
            }
        }
    }

    public String storageFormat() {
        String out = "";

        out += "1_" + red + "_";
        out += "2_" + green + "_";
        out += "3_" + blue + "_";
        out += "11_255_12_255_13_255_";
        if (copyPlayerCol == 1 || copyPlayerCol == 2) out += "4_" + copyPlayerCol + "_";
        out += "6_" + colorChannel + "_";
        if (blending) out += "5_1_";
        out += "7_" + formatFloat(opacity) + "_";
        out += "15_1_";
        if (useColorCopy) {
            out += "9_" + copyColorChannel + "_";
            out += "10_" + hsb.storageFormat() + "_";
        }
        out += "18_0_8_1";

        return out;
    }

    public static String formatFloat(int f) {
        String hundreds = String.valueOf(f/100);
        String tens = String.valueOf(Math.abs(f / 10 % 10));
        String ones = String.valueOf(Math.abs(f % 10));

        if (f % 100 == 0) return hundreds;
        return hundreds + "." + tens + ones;
    }

    public static int deFormatFloat(String f) {
        String[] parts = f.split(".");
        int fl = 0;
        if (parts.length == 1) {
            fl = Integer.parseInt(parts[0]) * 100;
        } else {
            fl = Integer.parseInt(parts[0]) * 100 + Integer.parseInt(parts[1]);
        }
        return fl;
    }
}
