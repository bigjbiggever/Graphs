package com.example.routes;

public class StationsPair {
    private int stationA;
    private int stationB;
    private int distance;

    /***
     * StationsPair generator
     * @param stationA first station
     * @param stationB second station
     * @param distance distance between the stations
     */
    public StationsPair(int stationA, int stationB, int distance) {
        this.stationA = stationA;
        this.stationB = stationB;
        this.distance = distance;
    }

    /***
     * StationsPair generator using another pair
     * @param pair original pair
     */
    public StationsPair(StationsPair pair) {
        this.stationA = pair.stationA;
        this.stationB = pair.stationB;
        this.distance = pair.distance;
    }

    /***
     * Station A getter
     * @return Station A
     */
    public int getStationA() {
        return stationA;
    }

    /***
     * Station B getter
     * @return Station B
     */
    public int getStationB() {
        return stationB;
    }

    /***
     * Distance Getter
     * @return Distance between station A and station B
     */
    public int getDistance() {
        return distance;
    }

    /***
     * Station A setter
     * @param stationA
     */
    public void setStationA(int stationA) {
        this.stationA = stationA;
    }

    /***
     * Station B setter
     * @param stationB
     */
    public void setStationB(int stationB) {
        this.stationB = stationB;
    }

    /***
     * Distance setter
     * @param distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /***
     * Reverses pair
     */
    public void reverse() {
        int tempStation = this.stationA;
        this.stationA = this.stationB;
        this.stationB = tempStation;
    }

    /***
     * ToString
     * @return string representing the station pair
     */
    @Override
    public String toString() {
        return "[from: " + this.stationA + " to " + this.stationB + " (" + this.distance + ")]";
    }
}
