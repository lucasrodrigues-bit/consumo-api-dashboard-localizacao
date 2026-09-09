import service.GeoLocationService;
import service.WeatherService;
import service.CurrencyService;
import model.LocalInfo;
import model.WeatherInfo;
import model.CambioInfo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        GeoLocationService geoService = new GeoLocationService();
        WeatherService weatherService = new WeatherService();
        CurrencyService currencyService = new CurrencyService();

        try {
            System.out.println("Digite o ip,cep(Brasil) ou nome da cidade desejada:");
            String localidade = input.nextLine();

            // 1º: resolve a localização — essa é a base pra tudo que vem depois
            LocalInfo local = geoService.buscarLocal(localidade);
            System.out.println(local);

            // 2º: usa a cidade já resolvida (não pede de novo pro usuário)
            //Nome da cidade sendo codificado para URL.
            String cidadeCodificada = URLEncoder.encode(local.getCity(), StandardCharsets.UTF_8);
            WeatherInfo clima = weatherService.DadosMeteorologicos(cidadeCodificada);
            System.out.println(clima);

            // 3º: usa a moeda já resolvida (BRL fixo se for CEP, código do país se for IP)
            CambioInfo cambio = currencyService.ConversorDeMoedas(local.getCurrency());
            System.out.println(cambio);

        } catch (Exception e) {
            System.out.println("Deu erro: " + e.getMessage());
        }
    }
}