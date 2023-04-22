package com.example.routes;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

import static javafx.application.Application.launch;

public class CityBuilderView implements ICityBuilderView {

    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int CELL_SIZE = 40;
    private ICityBuilderPresenter presenter;

    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private GridPane gridPane;
    private ChoiceBox<CellType> choiceBox;
    private Button doneButton;
    private Rectangle[][] cells = new Rectangle[ROWS][COLS];
    private ICityBuilderPresenter iCityBuilderPresenter;

    enum CellType {
        ROAD(Color.GRAY), BUILDING(Color.WHITE), STATION(Color.BLUE);

        private final Color color;

        CellType(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public int toInt() {
            return this.ordinal();
        }

        public static CellType fromInt(int n) {
            return (n == 0) ? CellType.ROAD : ((n == 2) ? CellType.STATION : CellType.BUILDING);
        }
    }

    public CityBuilderView () {}

    public CityBuilderView(ICityBuilderPresenter presenter) {
        super();
        this.presenter = presenter;
    }


    @Override
    public void setCell(int row, int col, CellType type) {
        cells[row][col].setFill(type.getColor());
    }

    public void init(Stage stage, int[][] map) {
        this.stage = stage;

        // Create the grid for the city
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        gridPane.setPadding(new Insets(10));

        // Create the cells for the grid and add them to the grid pane
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                int cellValue = map[row][col];

                CellType cellType = CellType.fromInt(cellValue);
                cell.setFill(cellType.getColor());

                cell.setId("" + (row * COLS + col));
                cells[row][col] = cell;

                gridPane.add(cell, col, row);

                cell.setOnMouseClicked(e -> {
                    int n = Integer.parseInt(cell.getId());
                    int r = n / COLS;
                    int c = n % COLS;
                    CellType choice = choiceBox.getValue();
                    cells[r][c].setFill(choice.getColor());
                    presenter.updateMap(r, c, choice.toInt());
                });
            }
        }
    }

    public void numberStations(ArrayList<int[]> stations) {
        gridPane.getChildren().removeIf(n -> n instanceof Text);

        for (int stationNo = 0; stationNo < stations.size(); stationNo++) {
            int row = stations.get(stationNo)[0];
            int col = stations.get(stationNo)[1];

            Text label = new Text(" " + stationNo + "");
            label.setX(CELL_SIZE / 2);
            label.setY(CELL_SIZE / 2 + 4);
            label.setStroke(Color.WHITE);

            gridPane.add(label, row, col);
        }
    }

    @Override
    public void show() {
        // Create the choice box for selecting the cell type
        // Create the choice box for selecting the cell type
        choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(CellType.ROAD, CellType.BUILDING, CellType.STATION);
        choiceBox.setValue(CellType.ROAD);

        // Create the "Done" button
        Button doneButton = new Button("Done");
        doneButton.setOnAction(event -> {
            presenter.findRoutes();
        });

        // Create the stack pane and add the grid pane and the "Done" button to it
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(gridPane, doneButton);
        StackPane.setMargin(doneButton, new Insets(100, 0, 0, 0)); // Add margin to the top of the "Done" button

        // Create the border pane and add the choice box to it at the top
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));
        borderPane.setTop(choiceBox);
        BorderPane.setAlignment(choiceBox, Pos.CENTER);

        // Add the stack pane to the center of the border pane
        borderPane.setCenter(stackPane);

        // Set the alignment of the "Done" button to the bottom center
        StackPane.setAlignment(doneButton, Pos.BOTTOM_CENTER);

        // Create the scene and show the stage
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setTitle("City Builder");
        stage.show();
    }
}
