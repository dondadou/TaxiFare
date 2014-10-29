package com.taxifare.model.pojos;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteResponse {
    private String status;
    private List<PlacePOJO> predictions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PlacePOJO> getPredictions() {
        return predictions;
    }

    public List<String> getPlacesDescription(){
        List<String> descriptionList = new ArrayList<String>();
        for (PlacePOJO prediction : predictions) {
            descriptionList.add(prediction.getDescription());
        }

        return descriptionList;
    }

    public void setPredictions(List<PlacePOJO> predictions) {
        this.predictions = predictions;
    }
}
