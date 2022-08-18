package diversanto.gdmanager;

public class GDObject {
    private int id = 0;
    int x = 0;
    int y = 0;
    private String text = null;

    public GDObject(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * Converts this GD object to the string representation used by GD to store levels.
     * @return the formatted object.
     */
    protected String storageFormat() {
        StringBuilder formatted = new StringBuilder();

        formatted.append("1," + id + ",");
        formatted.append("2," + x + ",");
        formatted.append("3," + y + ",");
        if (text != null) formatted.append("31," + Base64Functions.encode(text));

        formatted.append(";");
        return formatted.toString().replace(",;", ";");
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
