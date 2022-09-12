package diversanto.gdmanager.color;

public class HSB {
    private int hue = 0;
    private int saturation = 100;
    private int brightness = 100;
    private int saturationToggle = 0;
    private int brightnessToggle = 0;

    public HSB() {}

    public HSB(String data) {
        String[] values = data.split("a");
        if (values.length != 5) throw new NumberFormatException("HSB string data must have 5 values!");

        setHue(Integer.parseInt(values[0]));
        setSaturation(Color.deFormatFloat(values[1]));
        setBrightness(Color.deFormatFloat(values[2]));
        setSaturationToggle(Integer.parseInt(values[3]) == 1);
        setBrightnessToggle(Integer.parseInt(values[4]) == 1);
    }

    public void setRGB(int r, int g, int b) {
        float R = (float) r / 255f;
        float G = (float) g / 255f;
        float B = (float) b / 255f;

        float Cmax = Math.max(Math.max(R, G), B);
        float Cmin = Math.min(Math.min(R, G), B);

        float delta = Cmax - Cmin;

        //Calculate hue
        if (delta == 0) {
            hue = 0;
        } else if (Cmax == R) {
            hue = (int)(60 * (((G - B) / delta) % 6));
        } else if (Cmax == G) {
            hue = (int)(60 * ((B - R) / delta + 2));
        } else if (Cmax == B) {
            hue = (int) (60 * ((R - G) / delta + 4));
        }

        if (hue > 180) hue -= 360;

        //Calculate saturation
        if (Cmax == 0) {
            saturation = 0;
        } else {
            saturation = (int)(delta / Cmax * 100);
        }

        brightness = (int)(Cmax * 100);
    }

    public void setRGB(int c) {
        setRGB((c >> 16) & 0xFF, (c >> 8) & 0xFF, c & 0xFF);
    }

    @Override
    public String toString() {
        String out = "";

        out += hue + "a";
        out += Color.formatFloat(saturation) + "a";
        out += Color.formatFloat(brightness) + "a";
        out += saturationToggle + "a";
        out += brightnessToggle;

        return out;
    }


    public int getHue() {
        return hue;
    }
    public void setHue(int hue) {
        this.hue = Math.min(Math.max(-180, hue), 180);
    }



    public int getSaturation() {
        return saturation;
    }
    public void setSaturation(int saturation) {
        if (saturationToggle == 1) {
            this.saturation = Math.min(Math.max(-100, saturation), 100);
        } else {
            this.saturation = Math.min(Math.max(0, saturation), 200);
        }
    }



    public int getBrightness() {
        return brightness;
    }
    public void setBrightness(int brightness) {
        if (brightnessToggle == 1) {
            this.brightness = Math.min(Math.max(-100, brightness), 100);
        } else {
            this.brightness = Math.min(Math.max(0, brightness), 200);
        }
    }



    public int getSaturationToggle() {
        return saturationToggle;
    }
    public void setSaturationToggle(boolean saturationToggle) {
        if (saturationToggle) {
            this.saturationToggle = 1;
            saturation -= 100;
        } else {
            this.saturationToggle = 0;
            saturation += 100;
        }
    }



    public int getBrightnessToggle() {
        return brightnessToggle;
    }
    public void setBrightnessToggle(boolean brightnessToggle) {
        if (brightnessToggle) {
            this.brightnessToggle = 1;
            brightness -= 100;
        } else {
            this.brightnessToggle = 0;
            brightness += 100;
        }
    }
}
