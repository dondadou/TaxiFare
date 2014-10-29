package com.taxifare.model.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Directions implements Serializable {
    @JsonIgnore
    public final static Directions NULL = new Directions("NULL", new ArrayList<RoutePOJO>());
    private String status;

    @JsonProperty(value = "routes")
    private List<RoutePOJO> routes;

    public String getStatus() {
        return status;
    }

    public List<RoutePOJO> getRoute() {
        return routes;
    }

    public String getEncodedPolyLine(){
        return routes.get(0).getEncodedPolyLine();
    }

    private Directions(String status, List<RoutePOJO> routes) {
        this.status = status;
        this.routes = routes;
    }

    public Directions() {
    }
}
