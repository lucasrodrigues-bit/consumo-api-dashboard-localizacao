package model.dto.Local;

public class IpApiResponse {
    private String countryCode,country,regionName,city,region,timezone,currency;
    private Float lat,lon;

    public String getCountryCode() {
        return countryCode;
    }

    public String getCurrency(){return currency;}

    public String getCountry() {
        return country;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getCity() {
        return city;
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
