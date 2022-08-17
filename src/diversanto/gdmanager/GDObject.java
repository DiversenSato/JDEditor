package diversanto.gdmanager;

public class GDObject {
    int id = 0;
    int x = 0;
    int y = 0;

    public GDObject(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String storageFormat() {
        return String.format("1,%d,2,%d,3,%d;", id, x, y);
    }
}
