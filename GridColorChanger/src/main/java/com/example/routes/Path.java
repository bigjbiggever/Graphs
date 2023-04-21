package com.example.routes;

import java.util.ArrayList;

public class Path {
    private ArrayList <Integer> path;
    private int length;

    public Path(ArrayList<Integer> path, int length) {
        this.path = (ArrayList<Integer>) path.clone();
        this.length = length;
    }

    public Path(Path original) {
        this.path = (ArrayList<Integer>) original.getPath().clone();
        this.length = original.getLength();
    }

    // Connect the second path to the end of this path
    public void connectLines (Path second) {
        this.path.addAll(second.getPath());
        this.length += second.getLength();
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }

    public void addLength (int length) {
        this.length += length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int get(int index) {
        if (this.path.size() > index && index >= 0)
            return this.path.get(index);
        System.out.println("\n index: " + index + " path size: " + this.path.size());
        return -1;
    }

    public void set(int index,  int value) {
        this.path.set(index, value);
    }
}
