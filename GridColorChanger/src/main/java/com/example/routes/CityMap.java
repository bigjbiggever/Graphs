package com.example.routes;

import java.util.ArrayList;

public class CityMap {
    private static final int BOARD_SIZE = 10;
    private static final int[] rowT = {-1, 0, 1, 0};
    private static final int[] colT = {0, 1, 0, -1};

    private ArrayList<Station> stations;
    private Graph distances;
    private Cell[][] city;

    /***
     * CityMap generator
     * @param city the city's map
     */
    public CityMap(int[][] city) {
        this.city = new Cell[city.length][city[0].length];
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city[0].length; j++) {
                this.city[i][j] = new Cell(city[i][j]);
            }
        }
    }

    /***
     * Cell getter
     * @param x cell's x
     * @param y cell's y
     * @return the cell
     */
    public Cell getCell (int x, int y) {
        return this.city[y][x];
    }

    /***
     * Station array getter
     * @return the station array
     */
    public ArrayList<Station> getStations() {
        return this.stations;
    }

    /***
     * Graph getter
     * @return the Graph which represents the city
     */
    public Graph getDistances() {
        return new Graph(distances);
    }

    /***
     * Finds the stations in the city map and add them to the station array
     */
    public void findStations() {
        this.stations = new ArrayList<>();
        int cnt = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (city[i][j].isStation()) {
                    Station last = new Station(j, i, cnt);
                    stations.add(last);
                    city[i][j].storeDistance(last, 0);
                    cnt++;
                }
            }
        }
        this.distances = new Graph(this.stations.size());
        this.distances.init();
    }

    /***
     * Adds the next moves to a given Arraylist
     * @param s source station
     * @param x current x coordinate
     * @param y current y coordinate
     * @param distance distance from source
     * @param next ArrayList holding the next moves
     */
    public void nextMove(Station s, int x, int y, int distance, ArrayList<int[]> next) {
        for (int i = 0; i < rowT.length; i++) {
            if (!(x + colT[i] < 0 || x + colT[i] >= BOARD_SIZE) && !(y + rowT[i] < 0 || y + rowT[i] >= BOARD_SIZE)) {
                Cell cell =  this.city[y + rowT[i]][x + colT[i]];
                if (isValid(x + colT[i], y + rowT[i])) {
                    cell = new Cell(s, distance, 0);

                    this.city[y + rowT[i]][x + colT[i]] = cell;
                    // we extend the paths from all the stations that reached the x,y to this new cell
                    cell.extendPathFrom(this.city[y][x].getDistances());
                    next.add(new int[]{x + colT[i], y + rowT[i]});

                } else if (!cell.isBlocked()) {
                    Cell origin =  this.city[y][x];

                    // a path from another station passed through here
                    // source is the station s
                    // distance is the distance from s
                    // Distances from the rest is in the cell distances
                    if (!cell.isStation()) {
                        cell.extendPathFrom(origin.getDistances());
                        origin.extendPathFrom(cell.getDistances());
                    }
                    this.distances.addPath(s, distance, cell.getDistances());
                }
            }
        }
    }


    /***
     * Checks if a given coordinate is valid
     * @param x the x coordinate
     * @param y the y coordinate
     * @return boolean if it is valid or not
     */
    public boolean isValid(int x, int y) {
        return this.city[y][x].isRoad() && this.city[y][x].isPristine();
    }

    /***
     * Prints the map
     */
    public void printMap() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (city[i][j].getType() == 1){ System.out.print("| ");}
                else{
                    if(this.city[i][j].getType() == 2) {
                        System.out.print(this.city[i][j].getSource().getID());
                    } else {
                        if (city[i][j].getDistances().size() > 0) System.out.print("x"); // city[i][j]);
                        else System.out.print("-");
                    }
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    /***
     * Cell setter
     * @param value cell's value
     * @param x cell's x coordinate
     * @param y cell's y coordinate
     */
    public void setCell(int value, int x, int y) {
        city[y][x].setType(value);
    }
}
