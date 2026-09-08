package service;

//imports

import com.google.gson.Gson;
import model.WeatherInfo;
import model.dto.Weather.WeatherResponseDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    //Retorna dados sobre a Meteorologia do local
    public WeatherInfo DadosMeteorologicos(String localidade) throws IOException, InterruptedException {
        //chamando api_key configurada nas variáveis de ambiente
        String api_key = System.getenv("OPENWEATHER_API_KEY");

        //Configurando requisição Http
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openweathermap.org/data/2.5/weather?q="+localidade+"&appid="+api_key+"&lang=pt_br")).GET().build();
        HttpResponse <String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        //colocando o corpo da requisição e o status dela em variáveis
        int status = response.statusCode();
        String corpoRespostaJson = response.body();


        //Transformando Json em Objeto(Desserilização)
        Gson gson = new Gson();
        WeatherResponseDTO clima = gson.fromJson(corpoRespostaJson,WeatherResponseDTO.class);


        WeatherInfo dadosMeteorologicos = new WeatherInfo(
                clima.getWeather().get(0).getDescription(),
                clima.getWeather().get(0).getMain(),
                clima.getMain().getTemp(),
                clima.getMain().getFeels_like(),
                clima.getMain().getTemp_min(),
                clima.getMain().getTemp_max(),
                clima.getWind().getSpeed()
                ){

        };return dadosMeteorologicos;

    }

}
