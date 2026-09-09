package service;

import com.google.gson.Gson;
import model.FeriadoInfo;
import model.dto.Holiday.BrasilApiFeriadoDTO;
import model.dto.Holiday.CalendarificHolidayDTO;
import model.dto.Holiday.CalendarificResponseDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Year;

public class HolidayService {

    // Decide entre BrasilAPI (feriados nacionais BR) ou Calendarific (qualquer outro país)
    public FeriadoInfo buscarFeriados(String countryCode) throws IOException, InterruptedException {
        int ano = Year.now().getValue();

        if ("BR".equalsIgnoreCase(countryCode)) {
            return buscarFeriadosBrasil(ano);
        } else {
            return buscarFeriadosCalendarific(countryCode, ano);
        }
    }

    private FeriadoInfo buscarFeriadosBrasil(int ano) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://brasilapi.com.br/api/feriados/v1/" + ano))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        BrasilApiFeriadoDTO[] feriados = gson.fromJson(response.body(), BrasilApiFeriadoDTO[].class);

        LocalDate hoje = LocalDate.now();
        if (feriados != null) {
            for (BrasilApiFeriadoDTO f : feriados) {
                LocalDate data = LocalDate.parse(f.getDate());
                if (!data.isBefore(hoje)) {
                    return new FeriadoInfo(f.getName(), f.getDate());
                }
            }
        }
        return new FeriadoInfo("Nenhum feriado restante em " + ano, null);
    }

    private FeriadoInfo buscarFeriadosCalendarific(String countryCode, int ano) throws IOException, InterruptedException {
        String apiKey = System.getenv("CALENDARIFIC_API_KEY");
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://calendarific.com/api/v2/holidays?api_key=" + apiKey
                + "&country=" + countryCode + "&year=" + ano;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        CalendarificResponseDTO resposta = gson.fromJson(response.body(), CalendarificResponseDTO.class);

        if (resposta.getResponse() == null || resposta.getResponse().getHolidays() == null) {
            return new FeriadoInfo("Feriados não disponíveis para " + countryCode, null);
        }

        LocalDate hoje = LocalDate.now();
        for (CalendarificHolidayDTO h : resposta.getResponse().getHolidays()) {
            LocalDate data = LocalDate.parse(h.getDate().getIso().substring(0, 10));
            if (!data.isBefore(hoje)) {
                return new FeriadoInfo(h.getName(), data.toString());
            }
        }
        return new FeriadoInfo("Nenhum feriado restante em " + ano, null);
    }
}