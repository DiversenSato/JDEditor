package diversanto.gdmanager.color;

public class HSB {
    private int hue = 0;
    private float saturation = 100;
    private float brightness = 100;
    private int saturationToggle = 0;
    private int brightnessToggle = 0;

    public HSB() {}

    public HSB(String data) {
        String[] values = data.split("a");
        if (values.length != 5) throw new NumberFormatException("HSB string data must have 5 values!");

        setHue(Integer.parseInt(values[0]));
        setSaturation(Float.parseFloat(values[1]));
        setBrightness(Float.parseFloat(values[2]));
        saturationToggle = (values[3].equals("1") ? 1 : 0);
        brightnessToggle = (values[4].equals("1") ? 1 : 0);
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
            saturation = delta / Cmax;
        }

        brightness = Cmax;
    }

    public void setRGB(int c) {
        setRGB((c >> 16) & 0xFF, (c >> 8) & 0xFF, c & 0xFF);
    }

    @Override
    public String toString() {
        String out = "";

        out += hue + "a";
        out += saturation + "a";
        out += brightness + "a";
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



    public float getSaturation() {
        return saturation;
    }
    public void setSaturation(float saturation) {
        if (saturationToggle == 1) {
            this.saturation = Math.min(Math.max(-1, saturation), 1);
        } else {
            this.saturation = Math.min(Math.max(0, saturation), 2);
        }
    }



    public float getBrightness() {
        return brightness;
    }
    public void setBrightness(float brightness) {
        if (brightnessToggle == 1) {
            this.brightness = Math.min(Math.max(-1, brightness), 1);
        } else {
            this.brightness = Math.min(Math.max(0, brightness), 2);
        }
    }



    public int getSaturationToggle() {
        return saturationToggle;
    }
    public void setSaturationToggle(boolean saturationToggle) {
        if (saturationToggle) {
            this.saturationToggle = 1;
            saturation -= 1;
        } else {
            this.saturationToggle = 0;
            saturation += 1;
        }
    }



    public int getBrightnessToggle() {
        return brightnessToggle;
    }
    public void setBrightnessToggle(boolean brightnessToggle) {
        if (brightnessToggle) {
            this.brightnessToggle = 1;
            brightness -= 1;
        } else {
            this.brightnessToggle = 0;
            brightness += 1;
        }
    }
}
