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

    /**
     * Sobreescrita do toString() padrão de Object.Usado para exibir
     * o resultado da busca de forma legível no console durante os testes.
     */
    @Override
    public String toString(){
        return "LocalInfo{" +
                "country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", state='" + state + '\'' +
                ", region='" + region + '\'' +
                ", regionName='" + regionName + '\'' +
                ", city='" + city + '\'' +
                ", cep='" + cep + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", street='" + street + '\'' +
                ", timezone='" + timezone + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
