package model;

import java.util.List;

public class LocalInfo {
    private String country,countryCode,state,region,timezone,regionName,city,cep,neighborhood,street,currency;
    private Float lat,lon;

    public LocalInfo(String contry, String countryCode, String state, String regionName, String city
            , String neighborhood, String street, String cep, String region, String timezone, Float lat, Float lon, String currency)
    {
        this.country = contry;
        this.countryCode = countryCode;
        this.region = region;

        this.currency = currency;

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

    public String getRegion() {
        return region;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getCity() {
        return city;
    }

    public String getCep() {
        return cep;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public String getCurrency() {
        return currency;
    }

    public Float getLat() {
        return lat;
    }

    public Float getLon() {
        return lon;
    }


    /**
     * Sobreescrita do toString() padrão de Object.Usado para exibir
     * o resultado da busca de forma legível no console durante os testes.
     */
    @Override
    public String toString() {
        return String.format(
                """
                ===== Informações do Local =====
                País: %s
                Código do país: %s
                Estado: %s
                Região: %s
                Região (nome): %s
                Cidade: %s
                CEP: %s
                Bairro: %s
                Rua: %s
                Fuso horário: %s
                Latitude: %s
                Longitude: %s
                Moeda: %s
                ================================
                """,
                country,
                countryCode,
                state,
                region,
                regionName,
                city,
                cep,
                neighborhood,
                street,
                timezone,
                lat,
                lon,
                currency
        );
    }
}
