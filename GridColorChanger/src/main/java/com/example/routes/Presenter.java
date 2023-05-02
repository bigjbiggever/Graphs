package com.example.routes;

public class Presenter {
    private final RoutesModel routesModel = new RoutesModel();
    private final IRoutesView iview;

    public Presenter(IRoutesView iview) {
        this.iview = iview;
    }


}
