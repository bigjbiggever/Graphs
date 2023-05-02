package com.example.routes;

import java.util.HashMap;
import java.util.Map;


public class Cell {
    private int type;
    private Station source; // closest station to cell
    private HashMap<Station, Integer> distances;

    /**
     * Generator function
     * complexity is O(1).
     * @param  type, the type
     */
    public Cell(int type) {
        this.type = type;
        this.distances = new HashMap<>();
    }

    /**
     * Generator function
     * complexity is O(1).
     * @param station  the source station
     * @param distance the distance from source
     * @param type     the type of the station
     */
    public Cell(Station station, int distance, int type) {
        this(type);
        if (station != null) {
            this.distances.put(station, distance);
        }
        if (this.source == null && station != null) {
            this.source = station;
        }
    }

    /**
     * Type getter
     * complexity is O(1).
     * @return returns the type of the Cell
     */
    public int getType() {
        return type;
    }

    /**
     * source getter
     * complexity is O(1).
     * @return the source of the current cell
     */
    public Station getSource() {
        return this.source;
    }

    /**
     * Adds the distance to the HashMap
     * complexity is O(1).
     * @param  station the source station
     * @param distance the distance from the station
     */
    public void storeDistance(Station station, int distance) {
        if (this.source == null) {
            this.source = station; // first station to visit this cell
        }
        if (!this.distances.containsKey(station)) this.distances.put(station, distance);
    }

    /**
     * distance getter
     * complexity is O(1).
     * @return returns the distance HashMap
     */
    public HashMap<Station, Integer> getDistances() {
        return distances;
    }


    public void extendPathFrom(HashMap<Station, Integer> distances) {
        for (Map.Entry<Station, Integer> target : distances.entrySet()) {
            this.storeDistance(target.getKey(), target.getValue() + 1);
        }
    }

    /***
     * To string function
     * @return the string
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<Station, Integer> target : distances.entrySet()) {
            str.append("p").append(target.getKey().getID());
        }
        return str.toString();
    }

    /***
     * Checks if the type is Road
     * @return boolean if the type is Road
     */
    public boolean isRoad() {
        return this.getType() == 0;
    }

    /***
     * Checks if the type is Station
     * @return boolean if the type is Station
     */
    public boolean isStation() {
        return this.getType() == 2;
    }

    /***
     * Checks if the type is Building
     * @return boolean if the type is Building
     */
    public boolean isBlocked() {
        return this.getType() == 1;
    }

    /***
     * Checks if there are no neighbors
     * @return boolean if there are neighbors
     */
    public boolean isPristine() {
        return this.getDistances().size() == 0;
    }

    /***
     * Value setter
     * @param value
     */
    public void setType(int value) {
        this.type = value;
    }
}
