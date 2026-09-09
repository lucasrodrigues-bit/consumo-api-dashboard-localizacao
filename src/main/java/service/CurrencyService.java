package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.CambioInfo;
import model.dto.Currency.CurrencyRateResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class CurrencyService {
    public CambioInfo ConversorDeMoedas(String moedaOrigem) throws IOException, InterruptedException {
        if (moedaOrigem == null || moedaOrigem.equals("BRL")) {
            return new CambioInfo(1.0, "BRL");
        } else {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create
                    ("https://economia.awesomeapi.com.br/json/last/" + moedaOrigem + "-BRL")).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String corpoRespostaJson = response.body();

            Gson gson = new Gson();

            Type tipoMapa = new TypeToken<Map<String, CurrencyRateResponse>>() {}.getType();

            Map<String, CurrencyRateResponse> mapa;
            try {
                mapa = gson.fromJson(corpoRespostaJson, tipoMapa);
            } catch (IllegalStateException e) {
                // A AwesomeAPI não devolveu o formato esperado — geralmente significa
                // que a moeda pedida não está entre as que ela cobre
                throw new IOException("Moeda não suportada: " + moedaOrigem, e);
            }

            if (mapa == null || mapa.isEmpty()) {
                throw new IOException("Moeda não suportada: " + moedaOrigem);
            }

            //pegando valor dentro do Map
            CurrencyRateResponse resposta = mapa.values().iterator().next();

            //convertendo bid de String para double
            double valorNumerico = Double.parseDouble(resposta.getBid());

            CambioInfo convesor = new CambioInfo(
                    valorNumerico,
                    resposta.getCode()

            );return convesor;

        }

    }
}

