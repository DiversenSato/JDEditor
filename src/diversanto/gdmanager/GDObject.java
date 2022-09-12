package diversanto.gdmanager;

import diversanto.gdmanager.color.Color;
import diversanto.gdmanager.color.HSB;

import java.util.ArrayList;

public class GDObject {
    private int id = 1;
    private int x = 15;
    private int y = 15;
    private int red;
    private int green;
    private int blue;
    private float moveTime = -1;
    private boolean touchTriggered = false;
    private int rotationAngle = 0;
    private int colorChannel = -1;
    private String text = null;
    private float scale = 1;
    private float opacity = -1;
    private boolean isTrigger = false;
    private HSB hsb = null;
    private float fadeIn = -1;
    private float fadeHold = -1;
    private float fadeOut = -1;
    private int targetGroupID = -1;
    private final ArrayList<Integer> groups = new ArrayList<>();
    private boolean spawnTriggered = false;
    private int noGlow = 0;

    public GDObject(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    protected GDObject(String data) {
        System.out.println(data);
        String[] keysVals = data.split(",");

        for (int i = 0; i < keysVals.length; i += 2) {
            int key = Integer.parseInt(keysVals[i]);
            String value = keysVals[i+1];

            switch (key) {
                case 1 -> id = Integer.parseInt(value);
                case 2 -> x = Integer.parseInt(value);
                case 3 -> y = Integer.parseInt(value);
                case 7 -> red = Integer.parseInt(value);
                case 8 -> blue = Integer.parseInt(value);
                case 9 -> green = Integer.parseInt(value);
                case 10 -> moveTime = Float.parseFloat(value);
                case 11 -> touchTriggered = true;
                case 35 -> opacity = Float.parseFloat(value);
                case 36 -> isTrigger = true;
                case 45 -> fadeIn = Float.parseFloat(value);
                case 46 -> fadeHold = Float.parseFloat(value);
                case 47 -> fadeOut = Float.parseFloat(value);
                case 51 -> targetGroupID = Integer.parseInt(value);
                case 62 -> spawnTriggered = true;
            }
        }
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
        if (id == 899 || id == 1006) {  //As far as I'm aware, only the color and pulse trigger have color
            formatted.append("7," + red + ",");
            formatted.append("8," + green + ",");
            formatted.append("9," + blue + ",");
        }
        if (moveTime != -1) formatted.append("10," + moveTime + ",");
        if (touchTriggered) formatted.append("11,1,");
        if (rotationAngle != 0) formatted.append("6," + rotationAngle + ",");
        if (colorChannel != -1) formatted.append("21," + colorChannel + ",");
        if (text != null) formatted.append("31," + Base64Functions.encode(text) + ",");
        if (scale != 1) formatted.append("32," + scale + ",");
        if (opacity != -1) formatted.append("35," + opacity + ",");
        if (isTrigger) formatted.append("36,1,");
        if (hsb != null) formatted.append("41,1,43," + hsb + ",");
        if (fadeIn != -1) formatted.append("45," + fadeIn + ",");
        if (fadeHold != -1) formatted.append("46," + fadeHold + ",");
        if (fadeOut != -1) formatted.append("47," + fadeOut + ",");
        if (targetGroupID != -1) formatted.append("51," + targetGroupID + ",");

        if (groups.size() == 1) {
            formatted.append("57," + groups.get(0));
        } else if (groups.size() > 1) {
            formatted.append("57,");
            for (int i = 0; i < groups.size()-1; i++) {
                formatted.append(groups.get(i) + ".");
            }
            formatted.append(groups.get(groups.size()-1));
        }

        if (spawnTriggered) formatted.append("62,1,");
        if (noGlow == 1) formatted.append("96,1,");

        System.out.println(formatted.append("end").toString().replace(",end" , ""));
        return formatted.append(";").toString().replace(",;", ";");
    }

    public boolean addGroup(int g) {
        if (groups.size() >= 10) {
            return false;
        }

        groups.add(g);
        return true;
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

    public void setScale(float s) {
        scale = s;
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
