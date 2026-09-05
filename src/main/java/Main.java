import service.GeoLocationService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        GeoLocationService geoService = new GeoLocationService();

        //Tratamento de exeções
        try {

            System.out.println("Digite o ip ou cep desejado:");
            String entrada = input.nextLine();

            //chama a função direto classe GeoLocationService()
            System.out.println(geoService.buscarLocal(entrada));

        } catch (Exception e){
            System.out.println("Deu erro:"+ e.getMessage());

        }



    }
}
