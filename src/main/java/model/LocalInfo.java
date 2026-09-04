package model;

public class LocalInfo {
    private String status,country,countryCode,region,regionName,city,zip,timezone;


    public LocalInfo(String status, String contry, String countryCode, String region, String regionName, String city, String zip, String timezone) {
        this.status = status;
        this.country = contry;
        this.countryCode = countryCode;
        this.region = region;
        this.regionName = regionName;
        this.city = city;
        this.zip = zip;
        this.timezone = timezone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContry() {
        return country;
    }

    public void setContry(String contry) {
        this.country = contry;
    }

    public String getCodecontry() {
        return countryCode;
    }

    public void setCodecontry(String codecontry) {
        countryCode = codecontry;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
