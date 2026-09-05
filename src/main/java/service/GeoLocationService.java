package service;

//imports
import java.net.URI;

//imports do Http(faz a requisição e recebe resposta da api)
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import model.LocalInfo;

import java.io.IOException;

//imports do Gson(faz o parser do Json para objetos)
import com.google.gson.Gson;
import model.dto.BrasilApiCepResponse;
import model.dto.IpApiResponse;


public class GeoLocationService {

    //Método que indentifica se a busca é por Ip ou Cep
    public LocalInfo buscarLocal(String entrada) throws IOException, InterruptedException {

        //Decidindo se a busca é por ip ou cep
        if (entrada != null && !entrada.isEmpty()) {

            if (entrada.contains(".")) {
                LocalInfo resultado_BuscaIp = buscarPorIp(entrada);
                return resultado_BuscaIp;

            }

            else {
                LocalInfo resultado_BuscaCep = buscarPorCep(entrada);
                return resultado_BuscaCep;

            }
        }

        //caso não digite nada busca pelo ip da própria máquina
        return buscarPorIp(entrada);
    }

    //------------------------------------------------------------//---------------------------------------------------------
    //Faz a busca por Ip para caso seja fora do aís(Brasil)
    private LocalInfo buscarPorIp(String ip) throws IOException, InterruptedException {

        //Criando HttpCliente "Carteiro" sabe como entregar dados e trazer respostas de volta
        HttpClient client = HttpClient.newHttpClient();


        //Condição que se não for passado nenhum ip,usará o da própria pessoa
        String urlBase = "http://ip-api.com/json/";
        String urlFinal;


        //se o ip estiver vazio ou String vazia("") busca com o ip da máquina
        if (ip == null || ip.isEmpty()) {

            urlFinal = urlBase + "?fields=status,message,country,countryCode,region,regionName,city,zip,timezone&lang=pt-BR";
        }

        //se não, busca com o ip que o usuário digitou
        else {
            urlFinal = urlBase + ip + "?fields=status,message,country,countryCode,region,regionName,city,zip,timezone&lang=pt-BR";
        }

        //Enviando envelope(Request)
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlFinal)).GET().build();

        //Recebendo resposta do envelope(Response)
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        //Lendo resposta do envelope
        int status = response.statusCode();
        String corpoRespostaJsonIp = response.body();

        //Converte resposta do envelope(Json) em Objeto para a classe LocalInfo
        Gson gson = new Gson();
        IpApiResponse converterJsonParaObjetoIp = gson.fromJson(corpoRespostaJsonIp, IpApiResponse.class);


        LocalInfo localIp = new LocalInfo(
                converterJsonParaObjetoIp.getCountry(),
                converterJsonParaObjetoIp.getCountryCode(),
                null,                     // state não existe no ip-api
                converterJsonParaObjetoIp.getRegionName(),
                converterJsonParaObjetoIp.getCity(),
                null,                     // neighborhood não existe no ip-api
                null,                     // street não existe no ip-api
                null,                     // cep não existe no ip-api
                converterJsonParaObjetoIp.getRegion(),
                converterJsonParaObjetoIp.getTimezone(),
                converterJsonParaObjetoIp.getLat(),
                converterJsonParaObjetoIp.getLon()
        );
        return localIp;

    }



    //-------------------------------------------------------//-------------------------------------------------------------
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
        BrasilApiCepResponse converterJsonParaObjetoCep = gson.fromJson(corpoRespostaJsonCep,
                BrasilApiCepResponse.class);


        LocalInfo localCep = new LocalInfo(
                null,
                null,
                converterJsonParaObjetoCep.getState(),
                null,
                converterJsonParaObjetoCep.getCity(),
                converterJsonParaObjetoCep.getNeighborhood(),
                converterJsonParaObjetoCep.getStreet(),
                converterJsonParaObjetoCep.getCep(),
                null,
                null,
                null,
                null
        );
        return localCep;
    }





}

