package com.example.routes;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RoutesPresenter extends Application implements ICityBuilderPresenter, IRoutesPresenter {
    private CityMap cityMap;
    private CityBuilderView cityBuilderView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
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

    @Override
    public void updateMap(int row, int col, int value) {
        this.cityMap.setCell(value, col, row);
    }

    @Override
    public void findRoutes() {
        RoutesModel m = new RoutesModel();
        ArrayList<int[]> next = new ArrayList<>();
        IRoutesView routesView = new RoutesView(this);

        cityMap.findStations();

        for (int i = 0; i < cityMap.getStations().size(); i++) {
            next.add(new int[] {cityMap.getStations().get(i).getX(), cityMap.getStations().get(i).getY()});
        }

        cityBuilderView.numberStations(next);

        int dist = 1;
        ArrayList<int[]> hold;
        do {
            hold = (ArrayList<int[]>) next.clone();
            next.clear();
            for (int i = 0; i < hold.size(); i++)  {
                cityMap.nextMove(cityMap.getCell(hold.get(i)[0], hold.get(i)[1]).getSource(), hold.get(i)[0], hold.get(i)[1], dist, next);

            }
            dist++;
        } while (next.size() > 0);

        Graph cityMapGraph = cityMap.getDistances();
        Graph allPathsGraph = cityMapGraph.floydWarshall();
        List<List<Integer>> routes = m.genRoutes(cityMap, allPathsGraph, Constants.LINES);

//        List<List<int[]>> routesAndDistance = new ArrayList<>(routes.size());
//        for (int i = 0; i < routes.size(); i++) {
//            List<Integer> route = routes.get(i);
//            int prevStation = route.get(0);
//
//            List<int[]> currentRoute = new ArrayList<>(route.size());
//            for (int j = 0; j < route.size(); j++) {
//                int currentStation = route.get(j);
//                int distance = allPathsGraph.getDistance(prevStation, currentStation);
//                currentRoute.add(new int[] {currentStation, distance});
//                prevStation = currentStation;
//            }
//            routesAndDistance.add(currentRoute);
//            System.out.print("Line " + (i+1) + ": ");
//            System.out.println(routes.get(i));
//        }
        allPathsGraph.printDistances();;
        List<Path> lines = allPathsGraph.clarkeAndWright(allPathsGraph.findMinNode());
        for(int i = 0; i < lines.size(); i++) {
            System.out.println("line " + i + ": " + lines.get(i));
        }

        //routesView.setRoutes(routesAndDistance);
        //routesView.show(new Stage());
    }
}
