package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;



public class GeoLocationService {
    //Faz a busca por Ip para caso seja fora do aís(Brasil)
   public String buscarPorIp(String ip) throws IOException, InterruptedException {

       //Criando HttpCliente "Carteiro" sabe como entregar dados e trazer respostas de volta
       HttpClient client = HttpClient.newHttpClient();

       //Condição que se não for passado nenhum ip,usará o da própria pessoa
       String urlBase = "http://ip-api.com/json/";
       String urlFinal;

       if (ip == null || ip.isEmpty()) {
           urlFinal = urlBase + "?fields=status,message,country,countryCode,region,regionName,city,zip,timezone&lang=pt-BR";
       } else {
           urlFinal = urlBase + ip + "?fields=status,message,country,countryCode,region,regionName,city,zip,timezone&lang=pt-BR";
       }

       //Recebendo resposta do envelope(Response)
       HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://ip-api.com/json/?fields=status,message,country,countryCode,region,regionName,city,zip,timezone&lang=pt-BR")).GET().build();
       HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());



       //Lendo resposta do envelope
       int status = response.statusCode();
        String corpo = response.body();
        return corpo;
   }


   //Busca por Cep caso seja dentro do país(Brasil)
   public String buscarPorCep(String cep)  throws IOException, InterruptedException {
       //Criando HttpCliente "Carteiro" sabe como entregar dados e trazer respostas de volta
       HttpClient client = HttpClient.newHttpClient();

       //Recebendo resposta do envelope(Response)
       HttpRequest request =
               HttpRequest.newBuilder().uri(URI.create( "https://brasilapi.com.br/api/cep/v1/"+cep)).GET().build();
       HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

       //Lendo resposta do envelope
       int status = response.statusCode();
       String corpo = response.body();
       return corpo;

   }



}

