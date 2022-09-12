package diversanto.gdmanager;

import diversanto.gdmanager.color.Color;
import diversanto.gdmanager.color.HSB;

public class GDObject {
    private int id = 0;
    private int x = 0;
    private int y = 0;
    private int rotationAngle = 0;
    private int colorChannel = -1;
    private String text = null;
    private HSB hsb = null;
    private int noGlow = 0;

    public GDObject(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * Converts this GD object to the string representation used by GD to store levels.
     * @return the formatted object.
     */
    @Override
    public String toString() {
        StringBuilder formatted = new StringBuilder();

        formatted.append("1," + id + ",");
        formatted.append("2," + x + ",");
        formatted.append("3," + y + ",");
        if (rotationAngle != 0) formatted.append("6," + rotationAngle + ",");
        if (colorChannel != -1) formatted.append("21," + colorChannel + ",");
        if (text != null) formatted.append("31," + Base64Functions.encode(text) + ",");
        if (hsb != null) formatted.append("41,1,43," + hsb + ",");
        if (noGlow == 1) formatted.append("96,1,");

        formatted.append(";");
        return formatted.toString().replace(",;", ";");
    }

    public void setRotation(int angle) {
        rotationAngle = angle;
    }

    public void setNoGlow() {
        noGlow = 1;
    }

    public void setColorChannel(int colorChannel) {
        this.colorChannel = colorChannel;
    }
    public void setColorChannel(Color colorChannel) {
        this.colorChannel = colorChannel.getChannel();
    }

    public HSB getHSB() {
        if (hsb == null) hsb = new HSB();
        return hsb;
    }

    /**
     * Creates a new object that represents a text string.
     * It is a shorthand method instead of creating a new object with id: 914, and setting the text manually.
     * @param x The y position of the object.
     * @param y The y position of the object.
     * @param text The text to be displayed.
     * @return the text object.
     */
    public static GDObject createText(int x, int y, String text) {
        GDObject newText = new GDObject(914, x, y);
        newText.text = text;
        return newText;
    }
}
