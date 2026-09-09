package model.dto.Local;

public class PaisFallback {
    private final String nome;
    private final String moeda;

    public PaisFallback(String nome, String moeda) {
        this.nome = nome;
        this.moeda = moeda;
    }

    public String getNome() {
        return nome;
    }

    public String getMoeda() {
        return moeda;
    }
}