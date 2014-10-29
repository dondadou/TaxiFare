package com.taxifare.model.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

//TODO: Need to find a better name
public class PlacePOJO {
    private String description;

    @JsonProperty(value = "place_id")
    private String placeId;
    private String id;
    private String reference;
    private List<String> matched_substrings;
    private List<String> terms;
    private String types;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public String getPlaceId() {
        return placeId;
    }

    @JsonIgnore
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public String getReference() {
        return reference;
    }

    @JsonIgnore
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonIgnore
    public List<String> getMatched_substrings() {
        return matched_substrings;
    }

    @JsonIgnore
    public void setMatched_substrings(List<String> matched_substrings) {
        this.matched_substrings = matched_substrings;
    }

    @JsonIgnore
    public List<String> getTerms() {
        return terms;
    }

    @JsonIgnore
    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    @JsonIgnore
    public String getTypes() {
        return types;
    }

    @JsonIgnore
    public void setTypes(String types) {
        this.types = types;
    }
}
