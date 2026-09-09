package model.dto.Local;

public class GeocodingResponseDTO {
    private String name;
    private double lat;
    private double lon;
    private String country;
    private String state;

    public String getName() { return name; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public String getCountry() { return country; }
    public String getState() { return state; }
}
