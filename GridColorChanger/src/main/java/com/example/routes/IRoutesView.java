package com.example.routes;

import javafx.stage.Stage;

import java.util.List;

public interface IRoutesView {
    void show(Stage stage);

    void setRoutes(List<List<int[]>> routes);
}
