import model.CambioInfo;
import model.FeriadoInfo;
import model.LocalInfo;
import model.ResultadoConsulta;
import model.WeatherInfo;
import service.CurrencyService;
import service.GeoLocationService;
import service.HolidayService;
import service.WeatherService;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        GeoLocationService geoService = new GeoLocationService();
        WeatherService weatherService = new WeatherService();
        CurrencyService currencyService = new CurrencyService();
        HolidayService holidayService = new HolidayService();

        try {

            System.out.println("Faça sua pesquisa de localidades:");
            String localidade = input.nextLine();

            // Busca informações sobre o local
            LocalInfo local = geoService.buscarLocal(localidade);

            // Busca informações meteorológicas
            WeatherInfo clima =
                    weatherService.DadosMeteorologicos(local.getCity());

            // Busca informações de câmbio
            CambioInfo cambio =
                    currencyService.ConversorDeMoedas(local.getCurrency());

            // Busca informações sobre feriados
            FeriadoInfo feriado =
                    holidayService.buscarFeriados(local.getCountryCode());

            // Junta todas as informações
            ResultadoConsulta resultado = new ResultadoConsulta(
                    local,
                    clima,
                    cambio,
                    feriado
            );

            // Exibe o resultado completo
            System.out.println(resultado);

        } catch (Exception e) {

            System.out.println("Deu erro: " + e.getMessage());

        } finally {

            input.close();
        }
    }
}