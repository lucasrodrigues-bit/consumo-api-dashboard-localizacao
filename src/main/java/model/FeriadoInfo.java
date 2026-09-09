package model;

public class FeriadoInfo {
    private String nome;
    private String data;

    public FeriadoInfo(String nome, String data) {
        this.nome = nome;
        this.data = data;
    }

    public String getNome() { return nome; }
    public String getData() { return data; }

    @Override
    public String toString() {
        return "FeriadoInfo{nome='" + nome + "', data='" + data + "'}";
    }
}