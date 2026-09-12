package model;

public class ResultadoConsulta {

    private LocalInfo local;
    private WeatherInfo clima;
    private CambioInfo cambio;
    private FeriadoInfo feriado;

    public ResultadoConsulta(
            LocalInfo local,
            WeatherInfo clima,
            CambioInfo cambio,
            FeriadoInfo feriado) {

        this.local = local;
        this.clima = clima;
        this.cambio = cambio;
        this.feriado = feriado;
    }

    public LocalInfo getLocal() {
        return local;
    }

    public WeatherInfo getClima() {
        return clima;
    }

    public CambioInfo getCambio() {
        return cambio;
    }

    public FeriadoInfo getFeriado() {
        return feriado;
    }

    @Override
    public String toString() {
        return """
                
                ================================
                       RESULTADO DA CONSULTA
                ================================
                
                %s
                
                %s
                
                %s
                
                %s
                
                """.formatted(
                local,
                clima,
                cambio,
                feriado
        );
    }
}