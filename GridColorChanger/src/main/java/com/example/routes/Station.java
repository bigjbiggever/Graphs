package com.example.routes;

public class Station {
    private int x;
    private int y;
    private int ID;

    /***
     * Station generator function
     * @param x station's x
     * @param y station's y
     * @param ID station's ID
     */
    public Station(int x, int y, int ID) {
        this.x = x;
        this.y = y;
        this.ID = ID;
    }

    /***
     * X getter
     * @return station's X
     */
    public int getX() {
        return x;
    }

    /***
     * Y getter
     * @return station's Y
     */
    public int getY() {
        return y;
    }

    /***
     * ID getter
     * @return station's ID
     */
    public int getID() {
        return ID;
    }

    /***
     * X Setter
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /***
     * Y setter
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /***
     * ID setter
     * @param ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }
}
