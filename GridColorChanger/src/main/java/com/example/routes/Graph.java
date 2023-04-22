package com.example.routes;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private int[][] distanceMatrix;

    public Graph(int nodes) {
        this.distanceMatrix = new int[nodes][nodes];
    }

    public Graph(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public void addNode() {
        int[][] temp = new int[distanceMatrix.length + 1][distanceMatrix[0].length + 1];
    }

    public int getDistance(int node1, int node2) {
        return distanceMatrix[node1][node2];
    }

    public int[][] getDistanceMatrix(){
        return this.distanceMatrix.clone();
    }

    public void setDistance(int node1, int node2, int distance) {
        if (distanceMatrix[node1][node2] > distance) {
            distanceMatrix[node1][node2] = distance;
            distanceMatrix[node2][node1] = distance;
        }
    }

    public void init() {
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[0].length; j++) {
                distanceMatrix[i][j] = Integer.MAX_VALUE;
                if (i == j) distanceMatrix[i][j] = 0;
            }
        }
    }

    public void printDistances() {
        for (int i = 0; i < this.distanceMatrix.length; i++) {
            for (int j = 0; j < this.distanceMatrix[0].length; j++) {
                if (distanceMatrix[i][j] == Integer.MAX_VALUE) {
                    System.out.print("              -");
                } else {
                    System.out.printf("%15d", distanceMatrix[i][j]);
                }
            }
            System.out.println("");
        }
    }

    public void add(Station s1, Station s2, int distance) {
        setDistance(s1.getID(), s2.getID(), distance);
    }

    public void addPath(Station s, int distance, HashMap<Station, Integer> distances) {
        for (Map.Entry<Station, Integer> target: distances.entrySet()) {
            if (distanceMatrix[s.getID()][target.getKey().getID()] > distance + target.getValue()) {
               add(s, target.getKey(), distance + target.getValue());
            }
        }
    }

    // O(v^3)
    public Graph floydWarshall() {
        int[][] distances = new int [this.distanceMatrix.length][this.distanceMatrix.length];

        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[0].length; j++) {
                distances[i][j] = this.distanceMatrix[i][j];
            }
        }

        for (int k = 0; k < distances.length; k++) {
            for (int j = 0; j < distances.length; j++) {
                for (int i = 0; i < distances.length; i++) {
                    if (distances[i][k] != Integer.MAX_VALUE && distances[k][j] != Integer.MAX_VALUE) {
                        if (distances[i][k] + distances[k][j] < distances[i][j])
                            distances[i][j] = distances[i][k] + distances[k][j];
                    }
                }
            }
        }

        return new Graph(distances);
    }

    // returns the id of the node with the lowest sum of distances
    public int findMinNode() {
        int min = Integer.MAX_VALUE, minIdx = 0, sum;
        for (int i = 0; i < this.distanceMatrix.length; i++) {
            sum = 0;
            for (int j = 0; j < this.distanceMatrix.length; j++)
                sum += this.distanceMatrix[i][j] + this.distanceMatrix[j][i];

            if (sum < min) {
                min = sum;
                minIdx = i;
            }
        }
        return minIdx;
    }

    public List<Path> clarkeAndWright(int depot) {
        List<Path> routes = new ArrayList<>();
        // Connect every station to the depot directly
        Path currPath;
        Path maxSavingsPath = null;
        int maxSaved = 0;
        for (int i = 0; i < distanceMatrix.length; i++) {
            if (i != depot && getDistance(depot, i) < Integer.MAX_VALUE) {
                Path path;
                ArrayList<StationsPair> currLine = new ArrayList<>();
                currLine.add(new StationsPair(depot, i, getDistance(depot, i)));

                path = new Path(currLine);
                routes.add(path);
                System.out.println("path" + i + ": " + path.toString());
            }
        }
        // Create every pair and find the ones with the highest savings

        for (int i = 0; i < routes.size(); i++) {
            int firstPathIndex = i;
            int secondPathIndex = i;
            maxSavingsPath = null;
            maxSaved = 0;

            for (int j = 0; routes.get(i) != null && j < routes.size(); j++) {
                if (i != j && routes.get(j) != null) {
                    currPath = calcSavings(routes, i, j, depot);
                    if (currPath != null) {
                        int saved = (routes.get(i).getLength() + routes.get(j).getLength()) - currPath.getLength();
                        if (maxSavingsPath == null && saved > 0 || saved > maxSaved) {
                            System.out.println("current Path: " + currPath + " saved: " + saved);
                            maxSavingsPath = currPath;
                            maxSaved = saved;
                            secondPathIndex = j;
                        }
                    }
                }
            }
            if(maxSavingsPath != null) {
                System.out.println("max saving" + maxSavingsPath + " saved " + maxSaved);
                routes.set(firstPathIndex, maxSavingsPath);
                //routes.remove(secondPathIndex);
                routes.set(secondPathIndex, null);
            }
        }

        return (List<Path>) routes.stream().filter((Path path) -> path != null).collect(Collectors.toList());
    }

    public Path calcSavings(List<Path> routes, int route1, int route2, int depot) {
        Path retPath;
        Path path1 = routes.get(route1);
        Path path2 = routes.get(route2);
        StationsPair end1 = path1.getLastHop();
        StationsPair end2 = path2.getLastHop();

        StationsPair start1 = path1.getFirstHop();
        StationsPair start2 = path2.getFirstHop();

        int terminalStation1 = end1.getStationB() == depot ? end1.getStationA() : end1.getStationB();
        int terminalStation2 = end2.getStationB() == depot ? end2.getStationA() : end2.getStationB();
        int firstStation1 = start1.getStationB() == depot ? start1.getStationA() : start1.getStationB();
        int firstStation2 = start2.getStationB() == depot ? start2.getStationA() : start2.getStationB();

        int distanceBetweenEnds = getDistance(terminalStation1, terminalStation2);
        // get distance from the first non-depot station to the end of the next route
        int distanceBetweenStart1AndEnd2 = getDistance(firstStation1, terminalStation2);
        int distanceBetweenStart2AndEnd1 = getDistance(firstStation2, terminalStation1);

        int maxDistanceFromDepot = Math.max(start1.getDistance(), start2.getDistance());
        int shortestDistanceBetweenPaths = Math.min(Math.min(distanceBetweenEnds, distanceBetweenStart1AndEnd2), distanceBetweenStart2AndEnd1);

        // If connecting the lines isn't shorter, return null
        if (shortestDistanceBetweenPaths >= maxDistanceFromDepot) {
            return null;
        }

        System.out.println("\n" + end1.getStationB() + "->" + end2.getStationB() + " distance: " + distanceBetweenEnds);
        System.out.println(end1.getStationB() + "->" + start2.getStationB() + " distance: " + distanceBetweenStart2AndEnd1);
        System.out.println(end2.getStationB() + "-> " + start1.getStationB() + " distance: " + distanceBetweenStart1AndEnd2);

        // If the routes connection starts from route1

        if (distanceBetweenEnds <= Math.min(distanceBetweenStart2AndEnd1, distanceBetweenStart1AndEnd2)) {
             // connecting the ends is the shortest path
            Path firstPath, secondPath;
            if (start1.getDistance() <= start2.getDistance()) {
                firstPath = path1;
                secondPath = path2;
            } else {
                firstPath = path2;
                secondPath = path1;
            }
             retPath = new Path(firstPath);
             retPath.connectLines(secondPath.reverse(), distanceBetweenEnds, depot);
            System.out.println("end->end");
        } else if (distanceBetweenStart2AndEnd1 < distanceBetweenStart1AndEnd2) {
            // start with path 1 and connect path 2 without depot
            retPath = new Path(path1);
            retPath.connectLines(path2, distanceBetweenStart2AndEnd1, depot);
            System.out.println("end1->start2 p1:" + path1 + "p2: " + path2);
        } else {
            // start with path 2 and connect path 1 without depot
            retPath = new Path(path2);
            retPath.connectLines(path1, distanceBetweenStart1AndEnd2, depot);
            System.out.println("end2->start1 p1:" + path1 + "p2: " + path2);
        }

        return retPath;
    }

}
