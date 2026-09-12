package model.dto.Local;

import java.util.List;
import java.util.Map;

public class RestCountriesResponseDTO {

    private DataInfo data;

    public DataInfo getData() {
        return data;
    }

    public static class DataInfo {

        private List<CountryInfo> objects;

        public List<CountryInfo> getObjects() {
            return objects;
        }
    }

    public static class CountryInfo {

        private NameInfo names;
        private Map<String, CurrencyDetail> currencies;

        public NameInfo getNames() {
            return names;
        }

        public Map<String, CurrencyDetail> getCurrencies() {
            return currencies;
        }
    }

    public static class NameInfo {

        private String common;

        public String getCommon() {
            return common;
        }
    }

    public static class CurrencyDetail {

        private String name;
        private String symbol;

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}