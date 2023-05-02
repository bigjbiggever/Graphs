package com.example.routes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Path {
    private ArrayList <StationsPair> path;
    private int length;

    /***
     * Path generator using a list of stations
     * @param path
     */
    public Path(ArrayList<StationsPair> path) {
        this.path = (ArrayList<StationsPair>) path.clone();
        this.length = this.path.stream().mapToInt((StationsPair pair) -> pair.getDistance()).sum();
    }

    /***
     * Path generator using another path
     * @param original the original path
     */
    public Path(Path original) {
        this.path = (ArrayList<StationsPair>) original.getPath().clone();
        for (int i = 0; i < this.path.size(); i++) {
            this.path.set(i, new StationsPair(this.path.get(i)));
        }
        this.length = original.getLength();
    }

    /***
     * Connect the one path to the end of this path
     * @param second the second path
     * @param distance the distance between the end of the second path to the beginning of this path
     * @param depot the depot
     */
    public void connectLines (Path second, int distance, int depot) {
        ArrayList<StationsPair> secondPathList = second.getPath();
        StationsPair lastHop = this.getLastHop();
        StationsPair firstHop = second.getFirstHop();
        int startStation = firstHop.getStationB() == depot ? firstHop.getStationA() : firstHop.getStationB();
        secondPathList.set(0, new StationsPair(lastHop.getStationB(), startStation, distance));

        this.path.addAll(secondPathList);
        this.length += second.getLength() - firstHop.getDistance() + distance;
    }

    /***
     * Path getter
     * @return the list of stations
     */
    public ArrayList<StationsPair> getPath() {
        return (ArrayList<StationsPair>) path.clone();
    }

    /***
     * Length getter
     * @return length of this path
     */
    public int getLength() {
        return length;
    }

    /***
     * Get value from an index
     * @param index
     * @return the StationPair at the given index
     */
    public StationsPair get(int index) {
        if (this.path.size() > index && index >= 0)
            return this.path.get(index);
        return null;
    }

    /***
     * Set value at a given index
     * @param index
     * @param value
     */
    public void set(int index,  StationsPair value) {
        this.path.set(index, value);
    }

    /***
     * @return last pair
     */
    public StationsPair getLastHop() {
        return this.path.get(this.path.size() - 1);
    }

    /***
     * @return first pair
     */
    public StationsPair getFirstHop() {
        return this.path.get(0);
    }

    /***
     * Reverses path
     * @return reversed path
     */
    public Path reverse() {
        Path reversed = new Path(this);
        Collections.reverse(reversed.path);
        for (StationsPair stationsPair : reversed.path) {
            stationsPair.reverse();
        }

        return reversed;
    }

    /***
     * ToString function
     * @return string of the object
     */
    @Override
    public String toString() {
        String str = "";
        for (StationsPair stationsPair : this.path) {
            if (str.length() > 0) str += "->";
            str += stationsPair.toString();
        }

        return str + "(" + this.length + ")";
    }

    /***
     * Turns path to array
     * @return array representing path
     */
    public int[] toArray() {
        int[] retArr = new int[this.path.size() + 1];
        for (int i = 0; i < this.path.size(); i++) {
            retArr[i] = this.path.get(i).getStationA();
        }
        retArr[retArr.length - 1] = this.path.get(this.path.size()-1).getStationB();
        return retArr;
    }

    /***
     * Turns path to array of stations
     * @return array of stations
     */
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

    /***
     * @return the number of stations
     */
    public int getNumberOfStops() {
        return this.path.size();
    }
}
