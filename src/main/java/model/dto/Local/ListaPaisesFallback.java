package model.dto.Local;
import java.util.Map;

public class ListaPaisesFallback {

    private static final Map<String, PaisFallback> DADOS = Map.ofEntries(
            Map.entry("US", new PaisFallback("Estados Unidos", "USD")),
            Map.entry("GB", new PaisFallback("Reino Unido", "GBP")),
            Map.entry("FR", new PaisFallback("França", "EUR")),
            Map.entry("DE", new PaisFallback("Alemanha", "EUR")),
            Map.entry("ES", new PaisFallback("Espanha", "EUR")),
            Map.entry("IT", new PaisFallback("Itália", "EUR")),
            Map.entry("PT", new PaisFallback("Portugal", "EUR")),
            Map.entry("NL", new PaisFallback("Países Baixos", "EUR")),
            Map.entry("BE", new PaisFallback("Bélgica", "EUR")),
            Map.entry("GR", new PaisFallback("Grécia", "EUR")),
            Map.entry("JP", new PaisFallback("Japão", "JPY")),
            Map.entry("CN", new PaisFallback("China", "CNY")),
            Map.entry("IN", new PaisFallback("Índia", "INR")),
            Map.entry("RU", new PaisFallback("Rússia", "RUB")),
            Map.entry("CA", new PaisFallback("Canadá", "CAD")),
            Map.entry("AU", new PaisFallback("Austrália", "AUD")),
            Map.entry("CH", new PaisFallback("Suíça", "CHF")),
            Map.entry("SE", new PaisFallback("Suécia", "SEK")),
            Map.entry("AR", new PaisFallback("Argentina", "ARS")),
            Map.entry("MX", new PaisFallback("México", "MXN")),
            Map.entry("CL", new PaisFallback("Chile", "CLP")),
            Map.entry("UY", new PaisFallback("Uruguai", "UYU")),
            Map.entry("PY", new PaisFallback("Paraguai", "PYG")),
            Map.entry("PE", new PaisFallback("Peru", "PEN")),
            Map.entry("CO", new PaisFallback("Colômbia", "COP")),
            Map.entry("ZA", new PaisFallback("África do Sul", "ZAR")),
            Map.entry("EG", new PaisFallback("Egito", "EGP")),
            Map.entry("TR", new PaisFallback("Turquia", "TRY"))
    );

    // Construtor privado: essa classe não deveria ser instanciada, só serve pra agrupar
    // o dado estático e o método de busca (padrão comum pra "repositórios" fixos)
    private ListaPaisesFallback() {}

    public static PaisFallback buscar(String countryCode) {
        return DADOS.get(countryCode);
    }
}