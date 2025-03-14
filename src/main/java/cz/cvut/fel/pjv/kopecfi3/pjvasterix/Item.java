package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

public abstract class Item {
    protected int x, y;

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract void collect(Asterix asterix);
}
