package model;

import java.util.List;

public class LocalInfo {
    private String country,countryCode,state,region,timezone,regionName,city,cep,neighborhood,street;
    private Float lat,lon;

    public LocalInfo(String contry, String countryCode, String state, String regionName, String city
                      , String neighborhood, String street,String cep,String region,String timezone,Float lat,Float lon)
    {
        this.country = contry;
        this.countryCode = countryCode;
        this.region = region;

        this.timezone = timezone;
        this.lat = lat;
        this.lon = lon;

        this.state = state;
        this.regionName = regionName;
        this.cep = cep;

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

    public String getRegion() {
        return region;
    }

    public String getTimezone() {
        return timezone;
    }

    public Float getLat() {
        return lat;
    }

    public Float getLon() {
        return lon;
    }
}
