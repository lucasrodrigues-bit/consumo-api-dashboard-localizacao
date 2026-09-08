package model;

public class WeatherInfo {
    private String description,main;
    private double temp,feels_like,temp_min,temp_max,speed;

    //configurando o objeto para receber todos os atributos da resposta da API
    public WeatherInfo(String description, String main, double temp, double feels_like, double temp_min, double temp_max, double speed) {
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

    public double getTemp() {return temp;}

    public double getFeels_like() {return feels_like;}

    public double getTemp_min() {return temp_min;}

    public double getTemp_max() {return temp_max;}

    public double getSpeed() {return speed;}

    @Override
    public String toString(){
        return "Dados Meterológicos\n"+
                "Clima:"+getMain()+"\n"+
                "Descrição:"+getDescription()+"\n"+
                "Temperatura:"+getTemp()+"°C\n"+
                "Sensação Térmica:"+getFeels_like()+"°C\n"+
                "Temperatura máxima:"+getTemp_max()+"°C\n"+
                "Temperatura mínima"+getTemp_min()+"°C\n"+
                "Velocidade do vento:"+getSpeed()+"m/s";
    }
}




