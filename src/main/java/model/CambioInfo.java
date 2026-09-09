package model;

public class CambioInfo {
    private String code;
    private double ValorMoeda;

    public CambioInfo(double ValorMoeda,String code) {
        this.ValorMoeda = ValorMoeda;
        this.code = code;
    }

    public double getMoedas() {
        return ValorMoeda;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CambioInfo{code='" + code + "', valor=" + ValorMoeda + '}';
    }
}
