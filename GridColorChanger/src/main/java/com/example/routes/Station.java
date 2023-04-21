package com.example.routes;

public class Station {
    private int x;
    private int y;
    private int ID;

    public Station(int x, int y, int ID) {
        this.x = x;
        this.y = y;
        this.ID = ID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getID() {
        return ID;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
