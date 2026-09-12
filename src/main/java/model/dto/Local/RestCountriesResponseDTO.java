package model.dto.Local;

import java.util.List;

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
        private List<CurrencyDetail> currencies;

        public NameInfo getNames() {
            return names;
        }

        public List<CurrencyDetail> getCurrencies() {
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

        private String code;
        private String name;
        private String symbol;

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}