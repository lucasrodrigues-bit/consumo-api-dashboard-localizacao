package model.dto;

import com.google.gson.Gson;

public class BrasilApiCepResponse {
    private String cep,state,city,neighborhood,street;


    public String getCep() {
        return cep;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getStreet() {
        return street;
    }
}
