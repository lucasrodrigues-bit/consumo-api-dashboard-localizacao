import service.GeoLocationService;
import service.WeatherService;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        GeoLocationService geoService = new GeoLocationService();

        //Tratamento de exeções
        try {

            System.out.println("Digite o ip ou cep(Brasil) desejado:");
            String localidade = input.nextLine();

            //chama a função direto classe GeoLocationService()
            System.out.println(geoService.buscarLocal(localidade));

        } catch (Exception e){
            System.out.println("Deu erro:"+ e.getMessage());

        }

        Scanner entrada = new Scanner(System.in);
        WeatherService weatherService = new WeatherService();
        try {

            System.out.println("Digite o nome da cidade:");
            String localidade = entrada.nextLine();

            System.out.println(weatherService.DadosMeteorologicos(localidade));

        } catch (Exception e){
            System.out.println("Deu erro:"+ e.getMessage());

        }





    }
}
