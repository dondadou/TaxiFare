package com.taxifare.model.pojos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


//TODO: Need to find a better name
public class RoutePOJO {

    @JsonProperty(value = "summary")
    private String summary;

    @JsonIgnore
    @JsonProperty(value = "legs")
    private List<String> legs;

    @JsonIgnore
    @JsonProperty(value = "overview_polyline")
    private Point overviewPolyline;

    @JsonIgnore
    @JsonProperty(value = "bounds")
    private List<String> bounds;

    @JsonIgnore
    @JsonProperty(value = "copyrights")
    private String copyrights;

    @JsonIgnore
    @JsonProperty(value = "waypoint_order")
    private String waypoint_order;

    @JsonIgnore
    @JsonProperty(value = "warnings")
    private String warnings;

    @JsonProperty(value = "overview_polyline")
    public Point getPolyline() {
        return overviewPolyline;
    }

    @JsonProperty(value = "overview_polyline")
    public void setOverviewPolyline(Point overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    private static class Point {

        private String points;

        public String getPoints() {
            return points;
        }
    }

    public String getEncodedPolyLine() {
        return overviewPolyline.getPoints();
    }
}
