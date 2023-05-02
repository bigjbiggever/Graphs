package com.example.routes;

import java.util.ArrayList;
import java.util.List;

public class RoutesModel {

    /**
     * Returns a List of routes where each value in the inner Lists
     * is a station ID. The lines are round trip, so that they always
     * start and end at the depot.
     * <p>
     * This function run an exhaustive search algorithm so it's time
     * complexity is O(n!).
     * @param  c        cityMap object where we want to find routes
     * @param  g        graph of stations
     * @param  numLines number of lines to generate
     * @return          optimized bus routes
     */
    public List<List<Integer>> genRoutes(CityMap c, Graph g, int numLines) {
        int depot = g.findMinNode();
        List<Integer> stations = new ArrayList<>();
        for (int i = 0; i < c.getStations().size(); i++) {
            if (c.getStations().get(i).getID() != depot)
                stations.add(c.getStations().get(i).getID());
        }

        List<List<Integer>> bestRoutes = new ArrayList<>();
        List<List<Integer>> permutations = permutations(stations);
        int bestCost = Integer.MAX_VALUE;

        for (List<Integer> lineStations : permutations) {
            List<List<Integer>> routes = new ArrayList<>();

            for (int i = 0; i < numLines; i++)
                routes.add(new ArrayList<>());

            for (int i = 0; i < lineStations.size(); i++)
                routes.get(i % numLines).add(lineStations.get(i));

            int cost = 0;
            for (List<Integer> route : routes) {
                route.add(0, depot);
                route.add(depot);
                for (int i = 0; i < route.size() - 1; i++)
                    cost += g.getDistance(route.get(i), route.get(i + 1));
            }

            if (cost < bestCost) {
                bestCost = cost;
                bestRoutes = routes;
            }
        }
        return bestRoutes;
    }

    /**
     * This function returns all permutations of a given list.
     * <p>
     * The time complexity of this algorithm is O(n!).
     * @param  list
     * @return all the list's permutations
     */
    public List<List<Integer>> permutations(List<Integer> list) {
        List<List<Integer>> ret = new ArrayList<>();
        if (list.size() == 1) ret.add(list);
        else {
            for (int i = 0; i < list.size(); i++) {
                int element = list.get(i);
                List<Integer> remaining = new ArrayList<>(list.subList(0, i));
                remaining.addAll(list.subList(i+1, list.size()));
                for (List<Integer> permutation : permutations(remaining)) {
                    permutation.add(0, element);
                    ret.add(permutation);
                }
            }
        }
        return ret;
    }

}