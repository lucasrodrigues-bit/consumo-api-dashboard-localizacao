# Consumo de API + Dashboard de informações

Projeto de portfólio em **Java puro (POO)**, sem frameworks — a partir de um CEP, IP ou nome de cidade informado pelo usuário, agrega informações de localização, clima, câmbio, feriados e horário local, consultando múltiplas APIs externas.

Esse é o primeiro projeto de uma sequência de portfólio voltada a backend Java, servindo como base antes de projetos maiores em Spring Boot.

## Status atual

✅ Estrutura do projeto (Maven, pacotes, Git)
✅ Modelagem do banco de dados (DER)
✅ `GeoLocationService` — busca por IP, CEP e nome de cidade/país, com unificação de resposta
✅ `WeatherService` (OpenWeather)
✅ `CurrencyService` (câmbio via AwesomeAPI, com resolução automática da moeda de origem)
✅ `HolidayService` (feriados: BrasilAPI para o Brasil, Calendarific para os demais países)
⬜ Persistência (histórico de consultas, cache de câmbio/feriados) — deixada propositalmente para o final
⬜ Orquestração das buscas em paralelo (`CompletableFuture`)
⬜ Front mínimo de exibição

## Tecnologias

- **Java puro** (sem Spring Boot) — decisão consciente, para entender os fundamentos (HttpClient, threads, JDBC) antes de usar um framework que abstrai essas partes
- `java.net.http.HttpClient` — chamadas HTTP nativas do JDK
- **Gson** — parse de JSON para objetos Java, incluindo `Map`/`TypeToken` para respostas com chave dinâmica
- **MySQL** — banco de dados (instalado localmente no Windows, sem Docker) — implementação adiada para o final do projeto
- **JDBC** (`mysql-connector-j`) — acesso ao banco (ainda não implementado no código)
- **Maven** — gerenciamento de dependências e build
- **Git/GitHub** — controle de versão, com uma branch por camada (`model`, `service`, ...) até o momento

## APIs externas utilizadas

| Dado | API | Observação |
|---|---|---|
| Localização por IP | [ip-api.com](http://ip-api.com) | Usa o IP de quem chama se nenhum for informado. Inclui o campo `currency` (ISO), usado direto pelo `CurrencyService` |
| Localização por CEP | [BrasilAPI](https://brasilapi.com.br/docs) | Endpoint `/api/cep/v1/{cep}`. País/moeda fixados como `Brasil`/`BR`/`BRL` (dedução de domínio, não vem da API) |
| Localização por nome de cidade/país | [OpenWeather Geocoding](https://openweathermap.org/api/geocoding-api) | Endpoint `/geo/1.0/direct?q={nome}`. Nome codificado com `URLEncoder` (suporta espaços e acentos) |
| País/moeda a partir do nome | [restcountries.com](https://restcountries.com) | Fonte conhecida por instabilidade (redirects/502 intermitentes) — protegida por fallback (ver abaixo) |
| Clima | [OpenWeather](https://api.openweathermap.org) | Endpoint `/data/2.5/weather?q={cidade}&appid={API key}` |
| Câmbio | [AwesomeAPI](https://economia.awesomeapi.com.br) | Endpoint `/json/last/{moeda}-BRL`. Se a moeda de origem já for BRL, não faz chamada (conversão trivial) |
| Feriados (Brasil) | [BrasilAPI](https://brasilapi.com.br/docs) | Endpoint `/api/feriados/v1/{ano}` |
| Feriados (outros países) | [Calendarific](https://calendarific.com) | Endpoint `/api/v2/holidays?country={code}&year={ano}`. Exige `CALENDARIFIC_API_KEY` |

## Arquitetura

Camadas separadas por responsabilidade:

- **`model`** — classes de dado: os modelos de domínio unificados (`LocalInfo`, `WeatherInfo`, `CambioInfo`, `FeriadoInfo`), os DTOs (`model.dto.*`) que espelham o formato de cada API externa, e dados de referência estáticos (`PaisesFallback`, `ExonimosCidades`).
- **`service`** — lógica de negócio e integração com APIs externas. Cada service sabe montar a URL, chamar a API e mapear o resultado para o modelo unificado.
- **`db`** *(planejado)* — acesso ao banco via JDBC.
- **`Application`** — ponto de entrada, orquestrando os 4 services via console.

### Por que existe um DTO e um modelo unificado separados

Cada DTO (`IpApiResponse`, `BrasilApiCepResponse`, `GeocodingResponseDTO`, `WeatherResponseDTO`, `CurrencyRateResponse`, `BrasilApiFeriadoDTO`, `CalendarificHolidayDTO`...) espelha exatamente o JSON de uma API específica — existe só para o parse do Gson. Os modelos de domínio (`LocalInfo`, `WeatherInfo`, `CambioInfo`, `FeriadoInfo`) são o que o resto do sistema usa — nenhuma parte do sistema, fora do service responsável, precisa saber de qual API um dado veio. Cada service faz o mapeamento manual de DTO para modelo unificado.

### Resiliência a falhas de API externa

Duas fontes já se mostraram instáveis durante o desenvolvimento, e o projeto trata isso explicitamente em vez de deixar o programa quebrar:

- **`restcountries.com`** (usado na busca por nome, para resolver país/moeda): historicamente reporta quedas intermitentes (redirects, 502). Se a chamada falhar, o `GeoLocationService` cai para uma lista fixa de ~25 países comuns (`PaisesFallback`). Se o país nem estiver nessa lista, um aviso de cobertura é exibido e a busca continua sem esse dado, em vez de interromper a aplicação inteira.
- **`AwesomeAPI`** (câmbio): quando recebe um código de moeda que não cobre, responde com um formato inesperado em vez de erro HTTP. O `CurrencyService` trata esse caso e devolve uma mensagem clara em vez de propagar o erro técnico do parser.

### Busca por nome em português (exônimos)

Nomes de cidades brasileiras funcionam nativamente. Para cidades estrangeiras com nome tradicional em português diferente do nome local (ex: "Londres" → "London", "Nova York" → "New York"), existe uma tabela fixa de tradução (`ExonimosCidades`) consultada antes de montar a URL do Geocoding. Cobre as ~20 cidades mais comuns; fora dessa lista, o nome precisa ser digitado no idioma original.

## Decisões de modelagem do banco (DER)

- **Câmbio é cacheado por moeda**, não por local específico — evita duplicar cache para cidades diferentes do mesmo país (ex: Nova York e Los Angeles usam o mesmo cache de USD).
- **Feriados são cacheados por país.**
- **Histórico de consultas** guarda apenas `tipo_busca` (CEP,IP ou Nome) e `valor_busca` — decisão consciente de não duplicar dados de localização já resolvidos (evita violação de 3ª Forma Normal), mesmo sabendo que tabelas de log às vezes desnormalizam de propósito.
- **Clima não é persistido** — é um dado de passagem, muda rápido demais para valer a pena cachear.
- A implementação real do banco (JDBC, MySQL) foi deixada para o final do projeto, propositalmente — o foco atual é fechar todos os services consumindo as APIs diretamente, sem cache.

## Estrutura de pacotes

```
src/main/java/
  model/
    LocalInfo.java
    WeatherInfo.java
    CambioInfo.java
    FeriadoInfo.java
    PaisFallback.java
    PaisesFallback.java
    ExonimosCidades.java
    dto/
      Local/
        IpApiResponse.java
        BrasilApiCepResponse.java
        GeocodingResponseDTO.java
        RestCountriesResponseDTO.java
      Weather/
        WeatherResponseDTO.java
      Currency/
        CurrencyRateResponse.java
      Holiday/
        BrasilApiFeriadoDTO.java
        CalendarificResponseDTO.java
        CalendarificHolidayDTO.java
  service/
    GeoLocationService.java
    WeatherService.java
    CurrencyService.java
    HolidayService.java
  db/            (ainda vazio — persistência a implementar)
  Application.java
```

## Como rodar

1. Clonar o repositório e abrir no IntelliJ como projeto Maven.
2. Configurar as variáveis de ambiente `OPENWEATHER_API_KEY` e `CALENDARIFIC_API_KEY` (na configuração de execução do IntelliJ, ou nas variáveis de ambiente do Windows).
3. Rodar `Application.java` — o programa pede um CEP, IP ou nome de cidade/país no console e imprime localização, clima, câmbio e feriados em sequência.
4. (Banco de dados ainda não é necessário para rodar — a persistência está no roadmap.)

## Roadmap

1. Implementar a camada `db` (JDBC/MySQL) para histórico de consultas e cache de câmbio/feriados.
2. Criar um Orchestrator que chama os 4 services em paralelo (`CompletableFuture`) em vez de sequencialmente.
3. Construir um front mínimo (HTML + CSS + JS) para exibir o resultado.
4. Ampliar a cobertura das listas fixas de fallback (`PaisesFallback`, `ExonimosCidades`) conforme necessário.
