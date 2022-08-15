package diversanto.gdmanager;

public class HSB {
    private int hue = 0;
    private int saturation = 100;
    private int brightness = 100;
    private int saturationToggle = 0;
    private int brightnessToggle = 0;

    public String storageFormat() {
        String out = "";

        out += Color.formatFloat(hue) + "a";
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
        this.hue = Math.min(Math.max(-180, hue), 180);;
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
