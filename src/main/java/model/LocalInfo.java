package model;

import java.util.List;

public class LocalInfo {
    private String country,countryCode,state,regionName,city,zip,cep,neighborhood,street;

    public LocalInfo(String contry, String countryCode, String state, String regionName, String city
                      , String neighborhood, String street)
    {
        this.country = contry;
        this.countryCode = countryCode;

        this.state = state;
        this.regionName = regionName;

        this.city = city;
        this.neighborhood = neighborhood;
        this.street = street;
    }

    public String getCountry() {
        return country;
    }


    public String getCountryCode() {
        return countryCode;
    }


    public String getState() {
        return state;
    }


    public String getZip() {
        return zip;
    }


    public String getCep() {
        return cep;
    }


    public String getStreet() {
        return street;
    }


    public String getNeighborhood() {
        return neighborhood;
    }


    public String getCity() {
        return city;
    }


    public String getRegionName() {
        return regionName;
    }

}
