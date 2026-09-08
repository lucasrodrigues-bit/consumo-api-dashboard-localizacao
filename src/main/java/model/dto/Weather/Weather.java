package model.dto.Weather;

//configuração DTO das informações de clima
public class Weather {
    private int id;
    private String icon,description,main;//tempo,icon,descrição


    public int getId() {return id;}

    public String getIcon() {return icon;}

    public String getDescription() {return description;}

    public String getMain() {return main;}
}
