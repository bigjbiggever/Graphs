package com.example.routes;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RoutesPresenter extends Application implements ICityBuilderPresenter, IRoutesPresenter {
    private CityMap cityMap;
    private CityBuilderView cityBuilderView;

    /**
     * Main, starts the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the program, using the Application class
     * @param  stage the GUI stage
     */
    @Override
    public void start(Stage stage) {
        int[][] city = {
                {1, 2, 0, 0, 0, 0, 0, 1, 2, 0},
                {0, 0, 1, 1, 0, 0, 0, 1, 0, 0},
                {1, 0, 1, 0, 0, 0, 2, 0, 0, 0},
                {2, 0, 1, 0, 0, 1, 1, 1, 0, 1},
                {1, 1, 1, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 1, 0, 0, 1, 1, 2, 1, 1, 1},
                {0, 1, 0, 0, 0, 1, 0, 0, 2, 0},
                {2, 1, 0, 0, 1, 1, 0, 1, 1, 1}};

        cityMap = new CityMap(city);
        cityBuilderView = new CityBuilderView(this);
        cityBuilderView.init(stage, city);
        cityBuilderView.show();
    }

    /**
     * Changes the value of a cell
     * <p>
     * complexity is O(1).
     * @param  row      the row of the value
     * @param  col      the column of the value
     * @param  value    the value
     */
    @Override
    public void updateMap(int row, int col, int value) {
        this.cityMap.setCell(value, col, row);
    }

    /**
     * Finds ideal routes between the stations
     * <p>
     * This function does everything from the user input until
     * the bus routes.
     * complexity is O(n^3).
     */
    @Override
    public void findRoutes() {
        ArrayList<int[]> next = new ArrayList<>();
        IRoutesView routesView = new RoutesView(this);

        cityMap.findStations();

        // Create a line for each station
        for (int i = 0; i < cityMap.getStations().size(); i++) {
            next.add(new int[] {cityMap.getStations().get(i).getX(), cityMap.getStations().get(i).getY()});
        }

        // Number the stations in the view
        cityBuilderView.numberStations(next);

        int dist = 1;
        ArrayList<int[]> hold;
        // Run flood fill to find the distance between all neighboring stations
        do {
            hold = (ArrayList<int[]>) next.clone();
            next.clear();
            for (int i = 0; i < hold.size(); i++)  {
                cityMap.nextMove(cityMap.getCell(hold.get(i)[0], hold.get(i)[1]).getSource(), hold.get(i)[0], hold.get(i)[1], dist, next);

            }
            dist++;
        } while (next.size() > 0);

        Graph cityMapGraph = cityMap.getDistances();
        // Run Floyd Warshall algorithm on the graph in order to find the distance between non-neighboring stations
        Graph allPathsGraph = cityMapGraph.floydWarshall();

        // Run the Clarke and Wright savings algorithm with the depot from the Floyd Warshall graph
        List<Path> lines = allPathsGraph.clarkeAndWright(allPathsGraph.findMinNode());

        routesView.setRoutes(lines);
        routesView.show(new Stage());
    }
}
