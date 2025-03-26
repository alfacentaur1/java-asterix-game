package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

abstract class Character {
    protected double x, y;
    protected int health;

    public Character(int x, int y, int health) {
        this.x = x;
        this.y = y;
        this.health = health;

    }

    public boolean isAlive() {
        return health > 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHealth(int health) {
        this.health = health;
    }


}
