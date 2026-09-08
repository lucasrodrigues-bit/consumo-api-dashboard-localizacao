package model.dto.Weather;
import model.WeatherInfo;

import java.util.List;

//recebendo as respostas dos outros DTO's
public class WeatherResponseDTO {
    private List <WeatherInfo> weather;//weather é um objeto com 3 informações dentro
    private Main main;
    private Wind wind;

    public WeatherResponseDTO(){

    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }
}
