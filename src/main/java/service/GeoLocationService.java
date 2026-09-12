package service;

import com.google.gson.Gson;
import model.LocalInfo;
import model.dto.Local.PaisFallback;
import model.dto.Local.ListaPaisesFallback;
import model.dto.Local.BrasilApiCepResponse;
import model.dto.Local.IpApiResponse;
import model.dto.Local.GeocodingResponseDTO;
import model.dto.Local.RestCountriesResponseDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


public class GeoLocationService {

    // identifica se a busca é por Ip ou Cep
    public LocalInfo buscarLocal(String localidade) throws IOException, InterruptedException {

        // Decidindo se a busca é por ip ou cep
        if (localidade == null || localidade.isEmpty()) {
            return buscarPorIp(localidade); // sem entrada -> usa o IP de quem chamou
        }

        if (localidade.contains(".")) {
            return buscarPorIp(localidade);
        }

        if (localidade.matches("\\d+")) {
            return buscarPorCep(localidade);
        }

        return buscarPorNome(localidade);
    }


    // Faz a busca por Ip para caso seja fora do país (Brasil)
    private LocalInfo buscarPorIp(String ip) throws IOException, InterruptedException {

        // Criando HttpCliente "Carteiro"
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        String urlBase = "http://ip-api.com/json/";
        String urlFinal;

        // Se o ip estiver vazio ou String vazia("") busca com o ip da máquina
        if (ip == null || ip.isEmpty()) {

            urlFinal = urlBase + "?fields=currency,status,message,country,countryCode,region,regionName,city,zip," +
                    "timezone&lang=pt-BR";
        }

        // Se não, busca com o ip que o usuário digitou
        else {

            urlFinal = urlBase + ip + "?fields=currency,status,message,country,countryCode,region,regionName,city,zip," +
                    "timezone&lang=pt-BR";
        }

        // Enviando envelope(Request)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlFinal))
                .GET()
                .build();

        // Recebendo resposta do envelope(Response)
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        // Lendo resposta do envelope
        int status = response.statusCode();
        String corpoRespostaJsonIp = response.body();

        // Converte resposta JSON em objeto
        Gson gson = new Gson();

        IpApiResponse localPeloIp = gson.fromJson(
                corpoRespostaJsonIp,
                IpApiResponse.class
        );

        // Atribuindo
        LocalInfo localIp = new LocalInfo(
                localPeloIp.getCountry(),
                localPeloIp.getCountryCode(),
                null,                     // state não existe no ip-api
                localPeloIp.getRegionName(),
                localPeloIp.getCity(),
                null,                     // neighborhood não existe no ip-api
                null,                     // street não existe no ip-api
                null,                     // cep não existe no ip-api
                localPeloIp.getRegion(),
                localPeloIp.getTimezone(),
                localPeloIp.getLat(),
                localPeloIp.getLon(),
                localPeloIp.getCurrency()
        );

        return localIp;
    }


    // Busca por Cep caso seja dentro do país (Brasil)
    private LocalInfo buscarPorCep(String cep) throws IOException, InterruptedException {

        // Criando HttpCliente "Carteiro"
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        // Enviando envelope(Request)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://brasilapi.com.br/api/cep/v1/" + cep))
                .GET()
                .build();

        // Recebendo resposta do envelope(Response)
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        // Lendo resposta do envelope
        int status = response.statusCode();
        String corpoRespostaJsonCep = response.body();

        // Converte resposta JSON em objeto
        Gson gson = new Gson();

        BrasilApiCepResponse localPeloCep = gson.fromJson(
                corpoRespostaJsonCep,
                BrasilApiCepResponse.class
        );

        LocalInfo localCep = new LocalInfo(
                "Brasil",
                "BR",
                localPeloCep.getState(),
                null,
                localPeloCep.getCity(),
                localPeloCep.getNeighborhood(),
                localPeloCep.getStreet(),
                localPeloCep.getCep(),
                null,
                null,
                null,
                null,
                "BRL"
        );

        return localCep;
    }


    private LocalInfo buscarPorNome(String nome) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        String apiKeyOpenWeather = System.getenv("OPENWEATHER_API_KEY");
        String apiKeyRestCountries = System.getenv("REST_COUNTRIES_API_KEY");


        // =========================================================
        // 1º: nome -> lat/lon/countryCode
        // =========================================================

        String cidadeCodificadaParaUrl =
                URLEncoder.encode(nome, StandardCharsets.UTF_8);

        String urlGeocoding =
                "https://api.openweathermap.org/geo/1.0/direct?q=" +
                        cidadeCodificadaParaUrl +
                        "&limit=1&appid=" +
                        apiKeyOpenWeather;

        HttpRequest requestGeo = HttpRequest.newBuilder()
                .uri(URI.create(urlGeocoding))
                .GET()
                .build();

        HttpResponse<String> responseGeo = client.send(
                requestGeo,
                HttpResponse.BodyHandlers.ofString()
        );


        // Logs temporários
        System.out.println("Status OpenWeather: " + responseGeo.statusCode());
        System.out.println("Resposta OpenWeather:");
        System.out.println(responseGeo.body());


        Gson gson = new Gson();

        GeocodingResponseDTO[] resultadosGeo =
                gson.fromJson(
                        responseGeo.body(),
                        GeocodingResponseDTO[].class
                );


        if (resultadosGeo == null || resultadosGeo.length == 0) {
            throw new IOException("Nenhum local encontrado para: " + nome);
        }


        GeocodingResponseDTO localizacao = resultadosGeo[0];


        // =========================================================
        // 2º: countryCode -> nome e moeda
        // =========================================================

        String urlPais =
                "https://api.restcountries.com/countries/v5?q=" +
                        URLEncoder.encode(
                                localizacao.getCountry(),
                                StandardCharsets.UTF_8
                        );


        HttpRequest requestPais = HttpRequest.newBuilder()
                .uri(URI.create(urlPais))
                .header(
                        "Authorization",
                        "Bearer " + apiKeyRestCountries
                )
                .GET()
                .build();


        HttpResponse<String> responsePais = client.send(
                requestPais,
                HttpResponse.BodyHandlers.ofString()
        );


        // Logs temporários
        System.out.println("Status RestCountries: " + responsePais.statusCode());
        System.out.println("Resposta RestCountries:");
        System.out.println(responsePais.body());


        String nomePais = null;
        String codigoMoeda = null;


        // =========================================================
        // Verifica se a API respondeu corretamente
        // =========================================================

        if (responsePais.statusCode() == 200) {

            RestCountriesResponseDTO dadosPais =
                    gson.fromJson(
                            responsePais.body(),
                            RestCountriesResponseDTO.class
                    );


            // Pega o primeiro país encontrado
            if (dadosPais.getData() != null &&
                    dadosPais.getData().getObjects() != null &&
                    !dadosPais.getData().getObjects().isEmpty()) {

                RestCountriesResponseDTO.CountryInfo pais =
                        dadosPais.getData()
                                .getObjects()
                                .get(0);


                // =================================================
                // Nome do país
                // =================================================

                if (pais.getNames() != null) {

                    nomePais = pais.getNames().getCommon();
                }


                // =================================================
                // Moeda
                // =================================================

                if (pais.getCurrencies() != null &&
                        !pais.getCurrencies().isEmpty()) {

                    codigoMoeda =
                            pais.getCurrencies()
                                    .keySet()
                                    .iterator()
                                    .next();
                }
            }


        } else {

            // =====================================================
            // Fallback caso Rest Countries falhe
            // =====================================================

            PaisFallback fallback =
                    ListaPaisesFallback.buscar(
                            localizacao.getCountry()
                    );


            if (fallback != null) {

                nomePais = fallback.getNome();
                codigoMoeda = fallback.getMoeda();

            } else {

                System.out.println(
                        "Aviso de cobertura: país '" +
                                localizacao.getCountry() +
                                "' não está na lista de fallback. " +
                                "Nome completo e moeda não disponíveis."
                );
            }
        }


        // =========================================================
        // Retorna o LocalInfo
        // =========================================================

        return new LocalInfo(
                nomePais,
                localizacao.getCountry(),
                localizacao.getState(),
                null,                          // regionName
                localizacao.getName(),
                null,                          // neighborhood
                null,                          // street
                null,                          // cep
                null,                          // region
                null,                          // timezone
                (float) localizacao.getLat(),
                (float) localizacao.getLon(),
                codigoMoeda
        );
    }
}