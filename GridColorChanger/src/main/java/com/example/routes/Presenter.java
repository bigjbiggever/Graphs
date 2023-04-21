package com.example.routes;

public class Presenter {
    private RoutesModel routesModel = new RoutesModel();
    private IRoutesView iview;

    public Presenter(IRoutesView iview) {
        this.iview = iview;
    }


}
