package model;

public class WeatherInfo {
    private String description,main;
    private int temp,feels_like,temp_min,temp_max,speed;

    //configurando o objeto para receber todos os atributos da resposta da API
    public WeatherInfo(String description, String main, int temp, int feels_like, int temp_min, int temp_max, int speed) {
        this.description = description;
        this.main = main;
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.speed = speed;
    }


    public String getDescription() {return description;}

    public String getMain() {return main;}

    public int getTemp() {return temp;}

    public int getFeels_like() {return feels_like;}

    public int getTemp_min() {return temp_min;}

    public int getTemp_max() {return temp_max;}

    public int getSpeed() {return speed;}

    @Override
    public String toString(){
        return "Dados Meterológicos"+
                "Clima:"+getMain()+
                "Descrição:"+getDescription()+
                "Temperatura:"+getTemp()+
                "Sensação Térmica:"+getFeels_like()+
                "Temperatura máxima:"+getTemp_max()+
                "Temperatura mínima"+getTemp_min()+
                "Velocidade do veento:"+getSpeed();
    }
}




