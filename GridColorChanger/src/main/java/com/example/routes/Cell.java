package com.example.routes;

import java.util.HashMap;
import java.util.Map;

public class Cell {
    private int type;
    private Station source; // closest station to cell
    private HashMap<Station, Integer> distances;

    public Cell(int type) {
        this.type = type;
        this.distances = new HashMap<>();
    }

    public Cell(Station station, int distance, int type) {
        this(type);
        if (station != null) {
            this.distances.put(station, distance);
        }
        if (this.source == null && station != null) {
            this.source = station;
        }
    }

    public boolean isVisited(Station s) {
        return this.distances.containsKey(s);
    }

    public int getType() {
        return type;
    }

    public Station getSource() {
        return this.source;
    }


    public int getDistance(Station station) {
        if (this.distances.containsKey(station)) return this.distances.get(station);
        return Integer.MAX_VALUE;
    }

    public void storeDistance(Station station, int distance) {
        if (this.source == null) {
            this.source = station; // first station to visit this cell
        }
        if (!this.distances.containsKey(station)) this.distances.put(station, distance);
    }

    public HashMap<Station, Integer> getDistances() {
        return distances;
    }

    public void extendPathFrom(HashMap<Station, Integer> distances) {
        for (Map.Entry<Station, Integer> target: distances.entrySet()) {
            this.storeDistance(target.getKey(), target.getValue() + 1);
        }
    }

    public String toString() {
        String str = "";
        for (Map.Entry<Station, Integer> target: distances.entrySet()) {
            str = str + "p" + target.getKey().getID(); // + ":" + target.getValue();
        }
        return str;
    }

    public boolean isRoad() {
        return this.getType() == 0;
    }

    public boolean isStation() {
        return this.getType() == 2;
    }

    public boolean isBlocked() {
        return this.getType() == 1;
    }

    public boolean isPristine() {
        return this.getDistances().size() == 0;
    }

    public void setType(int value) {
        this.type = value;
    }
}
