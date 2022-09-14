package diversanto.gdmanager;

import diversanto.gdmanager.color.Color;
import diversanto.gdmanager.color.HSB;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GDObject {
    private int id = 1;
    private float x = 15;
    private float y = 15;

    private boolean flipHorizontally = false;
    private boolean flipVertically = false;
    private int rotation = -1;
    private int red;
    private int green;
    private int blue;
    private float moveTime = -1;
    private boolean touchTriggered = false;
    private boolean showPortalLines = false;
    private boolean usePlayerCol1 = false;
    private boolean usePlayerCol2 = false;
    private boolean useBlending = false;
    private int editorLayer1 = 0;
    private int baseColorChannel = -1;
    private int detailColorChannel = -1;
    private int colorTargetID = -1;
    private int zLayer = 0;
    private Integer zOrder = null;
    private int moveX = 0;
    private int moveY = 0;
    private int easingMode = 0;
    private String text = null;
    private float scale = 1;
    private float opacity = -1;
    private boolean isTrigger = false;
    private boolean useHSB = false;
    private HSB hsb = null;
    private float fadeIn = -1;
    private float fadeHold = -1;
    private float fadeOut = -1;
    private HSB copyColorHSB = new HSB();
    private int copyColorChannel = -1;
    private int targetGroupID = -1;
    private float teleportDistance = 100;
    private final ArrayList<Integer> groups = new ArrayList<>();
    private boolean lockPlayerX = false;
    private boolean lockPlayerY = false;
    private int editorLayer2 = 0;
    private boolean spawnTriggered = false;
    private boolean dontFade = false;
    private boolean dontEnter = false;
    private int targetPosID = -1;
    private float easingRate = -1;
    private boolean noGlow = false;
    private boolean useTarget = false;
    private int xOrY = 0;
    private boolean highDetail = false;
    private int linkedID = -1;

    private String extraData = "";

    public GDObject(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
        dontFade = true;
        dontEnter = true;
    }

    protected GDObject(String data) {
        String[] keysVals = data.split(",");

        for (int i = 0; i < keysVals.length; i += 2) {
            int key = 0;
            try {
                key = Integer.parseInt(keysVals[i]);
            } catch (NumberFormatException e) {
                continue;
            }
            String value = keysVals[i+1];

            switch (key) {
                default -> extraData += key + "," + value + ",";
                case 1 -> id = Integer.parseInt(value);
                case 2 -> x = Float.parseFloat(value);
                case 3 -> y = Float.parseFloat(value);
                case 4 -> flipHorizontally = true;
                case 5 -> flipVertically = true;
                case 6 -> rotation = Integer.parseInt(value);
                case 7 -> red = Integer.parseInt(value);
                case 8 -> green = Integer.parseInt(value);
                case 9 -> blue = Integer.parseInt(value);
                case 10 -> moveTime = Float.parseFloat(value);
                case 11 -> touchTriggered = true;
                case 13 -> showPortalLines = true;
                case 15 -> usePlayerCol1 = true;
                case 16 -> usePlayerCol2 = true;
                case 17 -> useBlending = true;
                case 20 -> editorLayer1 = Integer.parseInt(value);
                case 21 -> baseColorChannel = Integer.parseInt(value);
                case 22 -> detailColorChannel = Integer.parseInt(value);
                case 23 -> colorTargetID = Integer.parseInt(value);
                case 24 -> zLayer = Integer.parseInt(value);
                case 25 -> zOrder = Integer.parseInt(value);
                case 28 -> moveX = Integer.parseInt(value);
                case 29 -> moveY = Integer.parseInt(value);
                case 30 -> easingMode = Integer.parseInt(value);
                case 32 -> scale = Float.parseFloat(value);
                case 35 -> opacity = Float.parseFloat(value);
                case 36 -> isTrigger = true;
                case 41 -> useHSB = true;
                case 43 -> hsb = new HSB(value);
                case 45 -> fadeIn = Float.parseFloat(value);
                case 46 -> fadeHold = Float.parseFloat(value);
                case 47 -> fadeOut = Float.parseFloat(value);
                case 49 -> copyColorHSB.formatFrom(value);
                case 50 -> copyColorChannel = Integer.parseInt(value);
                case 51 -> targetGroupID = Integer.parseInt(value);
                case 54 -> teleportDistance = Float.parseFloat(value);
                case 57 -> {
                    for (String g : value.split("\\.")) {
                        groups.add(Integer.parseInt(g));
                    }
                }
                case 58 -> lockPlayerX = true;
                case 59 -> lockPlayerY = true;
                case 61 -> editorLayer2 = Integer.parseInt(value);
                case 62 -> spawnTriggered = true;
                case 64 -> dontFade = true;
                case 67 -> dontEnter = true;
                case 71 -> targetPosID = Integer.parseInt(value);
                case 85 -> easingRate = Float.parseFloat(value);
                case 96 -> noGlow = true;
                case 100 -> useTarget = true;
                case 101 -> xOrY = Integer.parseInt(value);
                case 103 -> highDetail = true;
                case 108 -> linkedID = Integer.parseInt(value);
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
        if (flipHorizontally) formatted.append("4,1,");
        if (flipVertically) formatted.append("5,1,");
        if (rotation != -1) formatted.append("6," + rotation + ",");
        if (id == 899 || id == 1006) {  //As far as I'm aware, only the color and pulse trigger have color
            formatted.append("7," + red + ",");
            formatted.append("8," + green + ",");
            formatted.append("9," + blue + ",");
        }
        if (moveTime != -1) formatted.append("10," + moveTime + ",");
        if (id == 901) {  //Move trigger ID
            formatted.append("28," + moveX + ",");
            formatted.append("29," + moveY + ",");
            formatted.append("30," + easingMode + ",");
        }
        if (touchTriggered) formatted.append("11,1,");
        if (showPortalLines) formatted.append("13,1,");
        if (usePlayerCol1) formatted.append("15,1,");
        if (usePlayerCol2) formatted.append("16,1,");
        if (useBlending) formatted.append("17,1,");
        if (editorLayer1 != 0) formatted.append("20," + editorLayer1 + ",");
        if (baseColorChannel != -1) formatted.append("21," + baseColorChannel + ",");
        if (detailColorChannel != -1) formatted.append("22," + detailColorChannel + ",");
        if (colorTargetID != -1) formatted.append("23," + colorTargetID + ",");
        if (zLayer != 0) formatted.append("24," + zLayer + ",");
        if (zOrder != null) formatted.append("25," + zOrder + ",");
        if (text != null) formatted.append("31," + Base64Functions.encode(text) + ",");
        if (scale != 1) formatted.append("32," + scale + ",");
        if (opacity != -1) formatted.append("35," + opacity + ",");
        if (isTrigger) formatted.append("36,1,");
        if (hsb != null) formatted.append("41,1,43," + hsb + ",");
        if (fadeIn != -1) formatted.append("45," + fadeIn + ",");
        if (fadeHold != -1) formatted.append("46," + fadeHold + ",");
        if (fadeOut != -1) formatted.append("47," + fadeOut + ",");
        if (copyColorChannel != -1) {
            formatted.append("50," + copyColorChannel + ",");
            formatted.append("49," + copyColorHSB + ",");
        }
        if (targetGroupID != -1) formatted.append("51," + targetGroupID + ",");
        if (id == 747) formatted.append("54," + teleportDistance + ",");

        if (groups.size() == 1) {
            formatted.append("57,").append(groups.get(0)).append(",");
        } else if (groups.size() > 1) {
            formatted.append("57,");
            for (int i = 0; i < groups.size()-1; i++) {
                formatted.append(groups.get(i) + ".");
            }
            formatted.append(groups.get(groups.size()-1)).append(",");
        }

        if (lockPlayerX) formatted.append("58,1,");
        if (lockPlayerY) formatted.append("59,1,");
        if (editorLayer2 != 0) formatted.append("61," + editorLayer2 + ",");
        if (spawnTriggered) formatted.append("62,1,");
        if (dontFade) formatted.append("64,1,");
        if (dontEnter) formatted.append("67,1,");
        if (targetPosID != -1) formatted.append("71," + targetPosID + ",");
        if (easingRate != -1) formatted.append("85," + easingRate + ",");
        if (noGlow) formatted.append("96,1,");
        if (useTarget) {
            formatted.append("100,1,");
            formatted.append("101," + xOrY + ",");
        }
        if (highDetail) formatted.append("103,1,");
        if (linkedID != -1) formatted.append("108," + linkedID + ",");

        formatted.append(extraData);
        return formatted.append(";").toString().replace(",;", ";");
    }

    public boolean addGroup(int g) {
        if (groups.size() >= 10) {
            return false;
        }

        groups.add(g);
        return true;
    }

    public boolean hasGroupID(int g) {
        for (Integer i : groups) {
            if (i == g) return true;
        }

        if (targetGroupID == g) return true;
        if (targetPosID == g) return true;

        return false;
    }

    public Point2D.Float getPosition() {
        return new Point2D.Float(x, y);
    }

    public void setRotation(int angle) {
        rotation = angle;
    }

    public void setNoGlow() {
        noGlow = true;
    }

    public void setBaseColorChannel(int baseColorChannel) {
        this.baseColorChannel = baseColorChannel;
    }
    public void setColorChannel(Color colorChannel) {
        this.baseColorChannel = colorChannel.getChannel();
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

    public void setEditorLayer(int layer) {
        editorLayer1 = layer;
    }
}
