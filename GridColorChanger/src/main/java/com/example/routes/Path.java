package com.example.routes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Path {
    private ArrayList <StationsPair> path;
    private int length;

    public Path(ArrayList<StationsPair> path) {
        this.path = (ArrayList<StationsPair>) path.clone();
        this.length = this.path.stream().mapToInt((StationsPair pair) -> pair.getDistance()).sum();
    }

    public Path(Path original) {
        this.path = (ArrayList<StationsPair>) original.getPath().clone();
        for (int i = 0; i < this.path.size(); i++) {
            this.path.set(i, new StationsPair(this.path.get(i)));
        }
        this.length = original.getLength();
    }

    // Connect the second path to the end of this path
    public void connectLines (Path second, int distance, int depot) {
        ArrayList<StationsPair> secondPathList = second.getPath();
        StationsPair lastHop = this.getLastHop();
        StationsPair firstHop = second.getFirstHop();
        int startStation = firstHop.getStationB() == depot ? firstHop.getStationA() : firstHop.getStationB();
        secondPathList.set(0, new StationsPair(lastHop.getStationB(), startStation, distance));

        this.path.addAll(secondPathList);
        this.length += second.getLength() - firstHop.getDistance() + distance;
    }

    public ArrayList<StationsPair> getPath() {
        return (ArrayList<StationsPair>) path.clone();
    }

    public int getLength() {
        return length;
    }

    public StationsPair get(int index) {
        if (this.path.size() > index && index >= 0)
            return this.path.get(index);
        return null;
    }

    public void set(int index,  StationsPair value) {
        this.path.set(index, value);
    }

    public StationsPair getLastHop() {
        return this.path.get(this.path.size() - 1);
    }

    public StationsPair getFirstHop() {
        return this.path.get(0);
    }

    public Path reverse() {
        Path reversed = new Path(this);
        Collections.reverse(reversed.path);
        for (StationsPair stationsPair : reversed.path) {
            stationsPair.reverse();
        }

        return reversed;
    }

    @Override
    public String toString() {
        String str = "";
        for (StationsPair stationsPair : this.path) {
            if (str.length() > 0) str += "->";
            str += stationsPair.toString();
        }

        return str + "(" + this.length + ")";
    }

    public int[] toArray() {
        int[] retArr = new int[this.path.size() + 1];
        for (int i = 0; i < this.path.size(); i++) {
            retArr[i] = this.path.get(i).getStationA();
        }
        retArr[retArr.length - 1] = this.path.get(this.path.size()-1).getStationB();
        return retArr;
    }

    public int[] toStationsList() {
        int [] stations = new int[this.path.size() + 1];
        Iterator<Integer> iterator = this.path.stream().mapToInt(pair -> pair.getStationB()).iterator();
        stations[0] = this.path.get(0).getStationA();
        for (int i = 1; iterator.hasNext(); i++) {
            int station = iterator.next();
            stations[i] = station;
        }

        return stations;
    }

    public int getNumberOfStops() {
        return this.path.size();
    }
}
