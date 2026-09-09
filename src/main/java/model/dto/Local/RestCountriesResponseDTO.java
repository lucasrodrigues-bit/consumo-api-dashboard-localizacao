package model.dto.Local;

import java.util.Map;

public class RestCountriesResponseDTO {
    private NameInfo name;
    private Map<String, CurrencyDetail> currencies;

    public NameInfo getName() { return name; }
    public Map<String, CurrencyDetail> getCurrencies() { return currencies; }


    public static class NameInfo {
        private String common;
        public String getCommon() { return common; }
    }

    public static class CurrencyDetail {
        private String name;
        private String symbol;
        public String getName() { return name; }
        public String getSymbol() { return symbol; }
    }
}