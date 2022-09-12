package diversanto.gdmanager.color;

public class Color {
    private int red = 40;
    private int green = 125;
    private int blue = 255;
    private int copyPlayerCol = -1;
    private boolean blending = false;
    private int colorChannel = 1000;
    private float opacity = 100;
    private int copyColorChannel = 1;
    private boolean useColorCopy = false;
    private HSB hsb = new HSB();

    public Color(String data) {
        String[] split = data.split("_");
        for (int i = 0; i < split.length / 2; i++) {
            int key = Integer.parseInt(split[i*2]);
            String value = split[i*2+1];

            switch (key) {
                case 1 -> red = Integer.parseInt(value);
                case 2 -> green = Integer.parseInt(value);
                case 3 -> blue = Integer.parseInt(value);
                case 4 -> copyPlayerCol = Integer.parseInt(value);
                case 5 -> blending = Integer.parseInt(value) == 1;
                case 6 -> colorChannel = Integer.parseInt(value);
                case 7 -> opacity = Float.parseFloat(value);
                case 9 -> {
                    useColorCopy = true;
                    copyColorChannel = Integer.parseInt(value);
                }
                case 10 -> {
                    useColorCopy = true;
                    hsb = new HSB(value);
                }
            }
        }
    }

    public Color(int channel) {
        colorChannel = channel;
    }

    @Override
    public String toString() {
        String out = "";

        out += "1_" + red + "_";
        out += "2_" + green + "_";
        out += "3_" + blue + "_";
        out += "11_255_12_255_13_255_";
        out += "4_" + copyPlayerCol + "_";
        out += "6_" + colorChannel + "_";
        if (blending) out += "5_1_";
        out += "7_" + opacity + "_";
        out += "15_1_";
        if (useColorCopy) {
            out += "9_" + copyColorChannel + "_";
            out += "10_" + hsb + "_";
        }
        out += "18_0_8_1";

        return out;
    }



    //
    // GETTERS AND SETTERS
    //
    public int red() {
        return red;
    }
    public void setRed(int value) {
        red = Math.min(Math.max(value, 0), 255);
    }

    public void setRGB(int r, int g, int b) {
        red = Math.min(Math.max(r, 0), 255);
        green = Math.min(Math.max(g, 0), 255);
        blue = Math.min(Math.max(b, 0), 255);
    }

    public int getChannel() {
        return colorChannel;
    }
}
