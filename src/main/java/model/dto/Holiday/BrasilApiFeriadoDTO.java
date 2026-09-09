package model.dto.Holiday;

// Espelha o formato de cada item do array que a BrasilAPI devolve
public class BrasilApiFeriadoDTO {
    private String date; // formato "yyyy-MM-dd", já compatível com LocalDate.parse
    private String name;
    private String type;

    public String getDate() { return date; }
    public String getName() { return name; }
    public String getType() { return type; }
}