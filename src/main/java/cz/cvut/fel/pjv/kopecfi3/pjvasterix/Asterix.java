package cz.cvut.fel.pjv.kopecfi3.pjvasterix;

import java.util.ArrayList;

public class Asterix extends Character implements WarriorStats{
    private int attackPower;
    private ArrayList<Item> inventory = new ArrayList<Item>();



    public Asterix(int x, int y, int health, int speed) {
        super(x, y, health, speed);
    }
    public int getStrength() {
        return attackPower;
    }

    public void setStrength(int attackPower) {
        this.attackPower = attackPower;
    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public void attack() {

    }


    //Depends on which potion will he drink
    public void drinkPotion(){

    }


}
