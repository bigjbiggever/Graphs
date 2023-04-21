package com.example.routes;

import java.util.*;

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

    public List<Path> clarkAndWright(int depot) {
        List<Path> routes = new ArrayList<>();
        // Connect every station to the depot directly
        Path currPath;
        Path maxSavings;
        for (int i = 0; i < distanceMatrix.length; i++) {
            ArrayList<Integer> currLine = new ArrayList<>();
            currLine.add(depot);
            currLine.add(i);
            routes.add(new Path(currLine, getDistance(depot, i)));
        }
        // Create every pair and find the ones with the highest savings

        for (int i = 0; i < routes.size(); i++) {
            int removedPath = 0;
            maxSavings = calcSavings(routes, i, 0, depot);
            for (int j = 0; j < routes.size(); j++) {
                currPath = calcSavings(routes, i, j, depot);
                if (currPath != null && maxSavings != null && currPath.getLength() < maxSavings.getLength()) {
                    maxSavings = currPath;
                    removedPath = j;
                }
            }
            if(maxSavings != null) routes.remove(removedPath);
        }

        return routes;
    }

    //TODO: Bug is here, doesn't change the path to a better one, also fix the -1 return
    public Path calcSavings(List<Path> routes, int route1, int route2, int depot) {
        // Skip the depot
        Path retPath;

//        for (int i = 0; i < routes.size(); i++) {
//            for (int j = 0; j < routes.get(i).getPath().size(); j++) {
//                System.out.print(routes.get(i).get(j) + " ");
//            }
//            System.out.println();
//        }

        int end1 = routes.get(route1).get(routes.get(route1).getPath().size() - 1);
        int end2 = routes.get(route2).get(routes.get(route2).getPath().size() - 1);

        int start1 = routes.get(route1).get(1);
        int start2 = routes.get(route2).get(1);

        // If connecting the lines isn't shorter, return null
        if (getDistance(end1, end2) > Math.min(getDistance(depot, routes.get(route1).getPath().get(1)), getDistance(depot, routes.get(route2).getPath().get(1)))) {
            return null;
        }

        System.out.println("end1: " + end1 + " end2: " + end2 + " distance: " + getDistance(end1, end2));
        System.out.println(Math.max(getDistance(depot, routes.get(route1).getPath().get(1)), getDistance(depot, routes.get(route2).getPath().get(1))));

        // If the routes connection starts from route1
        if (getDistance(depot, routes.get(route1).getPath().get(1)) < getDistance(depot, routes.get(route2).getPath().get(1))) {
            retPath = new Path(routes.get(route1));
            if (routes.get(route2).getPath().size() > 1)
                routes.get(route2).getPath().remove(0);
            retPath.connectLines(routes.get(route2));
            routes.get(route2).getPath().add(0, depot);
        }
        else {
            retPath = new Path(routes.get(route2));
            if (routes.get(route1).getPath().size() > 1)
                routes.get(route1).getPath().remove(0);
            retPath.connectLines(routes.get(route2));
            routes.get(route1).getPath().add(0, depot);
        }

//        for (int i = 0; i < retPath.getPath().size(); i++) {
//            System.out.print(retPath.get(i) + " ");
//        }
//        System.out.println();
        return retPath;
    }

}
