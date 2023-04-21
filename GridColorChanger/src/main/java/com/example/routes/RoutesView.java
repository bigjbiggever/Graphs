package com.example.routes;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class RoutesView implements IRoutesView {

    private IRoutesPresenter presenter;
    private List<List<int[]>> routes;

    public RoutesView() {}

    public RoutesView(IRoutesPresenter presenter) {
        super();
        this.presenter = presenter;
    }


    public void show(Stage routesStage) {
        routesStage.setTitle("Routes Map");
        FlowPane rootNode = new FlowPane();

        rootNode.setAlignment(Pos.CENTER);
        rootNode.setHgap(40);
        rootNode.setVgap(40);

        // rootNode.setAlignment(Pos.CENTER);
        Scene routesScene = new Scene(rootNode, 500, 450);
        routesStage.setScene(routesScene);

        Color[] colors = {Color.GREEN, Color.BLUE, Color.GRAY, Color.RED, Color.ORANGE};
        for (int k = 0; k < routes.size(); k++) {
            List<int[]> path = routes.get(k);
            int[] route = path.stream().mapToInt(pair -> pair[0]).toArray();
            int[] distances = path.stream().mapToInt(pair -> pair[1]).toArray();

            float routeDistance = Arrays.stream(distances).sum();
            // each route is a Group
            Group group = new Group();

            float distance = 0;
            int offset = 0;
            int offsetY = 20;
            int radius = 8;
            Line line = new Line(offset, offsetY, 360 + offset, offsetY);
            line.setStroke(colors[k % colors.length]);
            group.getChildren().add(line);
            for (int i = 0; i < route.length; i++) {
                distance += distances[i];
                int x = (int) Math.round(distance / routeDistance * 360.0);

                Circle station = new Circle();
                station.setCenterX(offset + x);
                station.setFill(colors[k]);
                station.setStroke(Color.BLACK);
                station.setCenterY(offsetY);
                station.setRadius(radius);
                group.getChildren().add(station);
                Text label = new Text(route[i] + "");
                label.setX(offset - 4 + x);
                label.setY(24);
                label.setStroke(Color.WHITE);
                group.getChildren().add(label);
            }
            rootNode.getChildren().add(group);
        }


        // Add the canvas and button to the scene graph.
        // rootNode.getChildren().addAll(canvas);

        // Show the stage and its scene.
        routesStage.show();
    }

    @Override
    public void setRoutes(List<List<int[]>> routes) {
        this.routes = routes;
    }
}
