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

import java.util.List;

public class RoutesView implements IRoutesView {

    private List<Path> routes;

    /***
     * RoutesView empty generator
     */
    public RoutesView() {}

    /***
     * RoutesView generator
     * @param presenter
     */
    public RoutesView(IRoutesPresenter presenter) {
        super();
    }

    /***
     * Draw bus lines
     * @param group
     * @param offsetX
     * @param offsetY
     * @param x
     * @param color
     * @param text
     */
    private void drawStationCircle(Group group, int offsetX, int offsetY, int x, Color color, String text) {
        final int RADIUS = 8;

        Circle station = new Circle();
        station.setCenterX(offsetX + x);
        station.setFill(color);
        station.setStroke(Color.BLACK);
        station.setCenterY(offsetY);
        station.setRadius(RADIUS);
        group.getChildren().add(station);
        Text label = new Text(text);
        label.setX(offsetX - (1 + text.length() * 2) + x);
        label.setY(24);
        label.setStroke(Color.WHITE);
        group.getChildren().add(label);
    }

    /***
     * Show routes
     * @param routesStage
     */
    public void show(Stage routesStage) {
        int k = 0; // color selector

        routesStage.setTitle("Routes Map");
        FlowPane rootNode = new FlowPane();

        rootNode.setAlignment(Pos.CENTER);
        rootNode.setHgap(40);
        rootNode.setVgap(40);

        // rootNode.setAlignment(Pos.CENTER);
        Scene routesScene = new Scene(rootNode, 500, 450);
        routesStage.setScene(routesScene);

        Color[] colors = {Color.GREEN, Color.BLUE, Color.GRAY, Color.RED, Color.ORANGE};
        for (Path path : routes) {

            float routeDistance =path.getLength();
            // each route is a Group
            Group group = new Group();

            float distance = 0;
            int offset = 0;
            int offsetY = 20;
            int segmentStartX = 0;

            Color color = colors[++k % colors.length];
            Line line = new Line(offset, offsetY, 360 + offset, offsetY);
            line.setStroke(color);
            group.getChildren().add(line);

            // draw depot station
            drawStationCircle(group, offset, offsetY, 0, color, path.getFirstHop().getStationA() + "");

            for (int i = 0; i < path.getNumberOfStops(); i++) {
                StationsPair pair = path.get(i);
                distance += pair.getDistance();
                int x = (int) Math.round(distance / routeDistance * 360.0);

                // add distance label
                Text label = new Text(pair.getDistance() + "");
                label.setX(offset + segmentStartX +  (x - segmentStartX) / 2 - 10);
                label.setY(18);
                label.setStroke(Color.BLACK);
                group.getChildren().add(label);

                drawStationCircle(group, offset, offsetY, x, color, pair.getStationB() + "");
                segmentStartX = x;
            }
            rootNode.getChildren().add(group);
        }

        // Show the stage and its scene.
        routesStage.show();
    }

    /***
     * Routes Setter
     * @param routes
     */
    @Override
    public void setRoutes(List<Path> routes) {
        this.routes = routes;
    }
}
