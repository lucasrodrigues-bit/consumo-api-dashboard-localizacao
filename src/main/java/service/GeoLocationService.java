

//imports

package service;

import com.google.gson.Gson;
import model.LocalInfo;
import model.dto.Local.BrasilApiCepResponse;
import model.dto.Local.IpApiResponse;
import model.dto.Local.GeocodingResponseDTO;
import model.dto.Local.RestCountriesResponseDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class GeoLocationService {
    //indentifica se a busca é por Ip ou Cep
    public LocalInfo buscarLocal(String localidade) throws IOException, InterruptedException {

        //Decidindo se a busca é por ip ou cep
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

    //Faz a busca por Ip para caso seja fora do país(Brasil)
    private LocalInfo buscarPorIp(String ip) throws IOException, InterruptedException {

        //Criando HttpCliente "Carteiro" sabe como entregar dados e trazer respostas de volta
        HttpClient client = HttpClient.newHttpClient();


        //Condição que se não for passado nenhum ip,usará o da própria pessoa
        String urlBase = "http://ip-api.com/json/";
        String urlFinal;


        //se o ip estiver vazio ou String vazia("") busca com o ip da máquina
        if (ip == null || ip.isEmpty()) {

            urlFinal = urlBase + "?fields=currency,status,message,country,countryCode,region,regionName,city,zip," +
                    "timezone&lang=pt-BR";
        }

        //se não, busca com o ip que o usuário digitou
        else {
            urlFinal = urlBase + ip + "?fields=currency,status,message,country,countryCode,region,regionName,city,zip," +
                    "timezone&lang=pt-BR";
        }

        //Enviando envelope(Request)
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlFinal)).GET().build();

        //Recebendo resposta do envelope(Response)
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        //Lendo resposta do envelope
        int status = response.statusCode();
        String corpoRespostaJsonIp = response.body();

        //Converte resposta do envelope(Json) em Objeto para a classe LocalInfo---Desserilização
        Gson gson = new Gson();
        IpApiResponse localPeloIp = gson.fromJson(corpoRespostaJsonIp, IpApiResponse.class);

        //Atribuindo
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

    //Busca por Cep caso seja dentro do país(Brasil)
    private LocalInfo buscarPorCep(String cep)  throws IOException, InterruptedException {


        //Criando HttpCliente "Carteiro" sabe como entregar dados e trazer respostas de volta
        HttpClient client = HttpClient.newHttpClient();

        //Enviando envelope(Request)
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create( "https://brasilapi.com.br/api/cep/v1/"+cep)).GET().build();

        //Recebendo resposta do envelope(Response)
        HttpResponse <String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        //Lendo resposta do envelope
        int status = response.statusCode();
        String corpoRespostaJsonCep = response.body();

        //Converte resposta do envelope(Json) em Objeto para a classe LocalInfo
        Gson gson = new Gson();
        BrasilApiCepResponse localPeloCep = gson.fromJson(corpoRespostaJsonCep,
                BrasilApiCepResponse.class);


        LocalInfo localCep = new LocalInfo
                (
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
        HttpClient client = HttpClient.newHttpClient();
        String apiKey = System.getenv("OPENWEATHER_API_KEY");

        // 1º: nome -> lat/lon/countryCode, via Geocoding API do OpenWeather
        String urlGeocoding = "https://api.openweathermap.org/geo/1.0/direct?q=" + nome + "&limit=1&appid=" + apiKey;
        HttpRequest requestGeo = HttpRequest.newBuilder().uri(URI.create(urlGeocoding)).GET().build();
        HttpResponse<String> responseGeo = client.send(requestGeo, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        // resposta é um array -> parse pra array de DTO, não pra um objeto só
        GeocodingResponseDTO[] resultadosGeo = gson.fromJson(responseGeo.body(), GeocodingResponseDTO[].class);

        if (resultadosGeo == null || resultadosGeo.length == 0) {
            throw new IOException("Nenhum local encontrado para: " + nome);
        }
        GeocodingResponseDTO localizacao = resultadosGeo[0];

        // 2º: countryCode -> moeda oficial do país, via restcountries.com
        String urlPais = "https://restcountries.com/v3.1/alpha/" + localizacao.getCountry() + "?fields=name,currencies";
        HttpRequest requestPais = HttpRequest.newBuilder().uri(URI.create(urlPais)).GET().build();
        HttpResponse<String> responsePais = client.send(requestPais, HttpResponse.BodyHandlers.ofString());

        RestCountriesResponseDTO dadosPais = gson.fromJson(responsePais.body(), RestCountriesResponseDTO.class);

        String nomePais = (dadosPais.getName() != null) ? dadosPais.getName().getCommon() : null;

        String codigoMoeda = null;
        if (dadosPais.getCurrencies() != null && !dadosPais.getCurrencies().isEmpty()) {
            // pega a primeira (e geralmente única) chave do Map de moedas, sem saber o nome dela de antemão
            codigoMoeda = dadosPais.getCurrencies().keySet().iterator().next();
        }

        return new LocalInfo(
                nomePais,
                localizacao.getCountry(),
                localizacao.getState(),
                null,                          // regionName não existe nessa fonte
                localizacao.getName(),
                null,                          // neighborhood
                null,                          // street
                null,                          // cep
                null,                          // region
                null,                          // timezone (geocoding não devolve)
                (float) localizacao.getLat(),
                (float) localizacao.getLon(),
                codigoMoeda
        );
    }
}

