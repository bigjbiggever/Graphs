module com.example.gridcolorchanger {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gridcolorchanger to javafx.fxml;
    opens com.example.routes to javafx.fxml;
    exports com.example.gridcolorchanger;
    exports com.example.routes;
}