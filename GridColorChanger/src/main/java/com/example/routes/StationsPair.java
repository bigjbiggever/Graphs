package com.example.routes;

public class StationsPair {
    private int stationA;
    private int stationB;
    private int distance;

    public StationsPair(int stationA, int stationB, int distance) {
        this.stationA = stationA;
        this.stationB = stationB;
        this.distance = distance;
    }

    public StationsPair(StationsPair pair) {
        this.stationA = pair.stationA;
        this.stationB = pair.stationB;
        this.distance = pair.distance;
    }

    public int getStationA() {
        return stationA;
    }

    public int getStationB() {
        return stationB;
    }

    public int getDistance() {
        return distance;
    }

    public void setStationA(int stationA) {
        this.stationA = stationA;
    }

    public void setStationB(int stationB) {
        this.stationB = stationB;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void reverse() {
        int tempStation = this.stationA;
        this.stationA = this.stationB;
        this.stationB = tempStation;
    }

    @Override
    public String toString() {
        return "[from: " + this.stationA + " to " + this.stationB + " (" + this.distance + ")]";
    }
}
