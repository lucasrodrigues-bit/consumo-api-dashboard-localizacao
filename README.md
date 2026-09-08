# Consumo de API + Dashboard de informações

Projeto de portfólio em **Java puro (POO)**, sem frameworks — o objetivo é, a partir de um CEP ou IP informado pelo usuário, agregar informações de localização, clima, câmbio, feriados e horário local, consultando múltiplas APIs externas.

Esse é o primeiro projeto de uma sequência de portfólio voltada a backend Java, servindo como base antes de projetos maiores em Spring Boot.

## Status atual

✅ Estrutura do projeto (Maven, pacotes, Git)
✅ Modelagem do banco de dados (DER)
✅ `GeoLocationService` — busca por IP e por CEP, com unificação de resposta
⬜ Persistência (histórico de consultas, cache de câmbio/feriados)
⬜ `WeatherService` (OpenWeather)
⬜ `CurrencyService` (câmbio)
⬜ `HolidayService` (feriados)
⬜ Orquestração das buscas em paralelo
⬜ Front mínimo de exibição

## Tecnologias

- **Java puro** (sem Spring Boot) — decisão consciente, para entender os fundamentos (HttpClient, threads, JDBC) antes de usar um framework que abstrai essas partes
- `java.net.http.HttpClient` — chamadas HTTP nativas do JDK
- **Gson** — parse de JSON para objetos Java
- **MySQL** — banco de dados (instalado localmente no Windows, sem Docker)
- **JDBC** (`mysql-connector-j`) — acesso ao banco (ainda não implementado no código)
- **Maven** — gerenciamento de dependências e build
- **Git/GitHub** — controle de versão, com uma branch por camada (`model`, `service`, ...) até o momento

## APIs externas utilizadas

| Dado | API                            | Observação |
|---|--------------------------------|---|
| Localização por IP | [ip-api.com](http://ip-api.com)| Usa o IP de quem chama se nenhum for informado |
| Localização por CEP | [BrasilAPI](https://brasilapi.com.br/docs) | Endpoint `/api/cep/v1/{cep}` |
| Clima | [OpenWeather](https://openweathermap.org) | Ainda não implementado |
| Câmbio | [AwesomeApi](https://awesomeapi.com.br) | Ainda não implementado |
| Feriados | BrasilAPI (nacional) / Calendarific (internacional) | Ainda não implementado |

## Arquitetura

Camadas separadas por responsabilidade:

- **`model`** — classes de dado. Inclui o modelo de domínio unificado (`LocalInfo`) e os DTOs (`model.dto`) que espelham exatamente o formato de cada API externa.
- **`service`** — lógica de negócio e integração com APIs externas. Cada service sabe montar a URL, chamar a API e mapear o resultado para o modelo unificado.
- **`db`** *(planejado)* — acesso ao banco via JDBC.
- **`Main`** — ponto de entrada, exercitando os services via console.

### Por que existe um DTO e um modelo unificado separados

`IpApiResponse` e `BrasilApiCepResponse` representam exatamente o JSON de cada API (usados só para o parse do Gson). `LocalInfo` é o modelo único que o resto do sistema usa — nenhuma parte do sistema, fora do `GeoLocationService`, precisa saber se um dado veio do ip-api ou da BrasilAPI. O `GeoLocationService` faz o mapeamento manual de DTO para modelo unificado.

## Decisões de modelagem do banco (DER)

- **Câmbio é cacheado por moeda**, não por local específico — evita duplicar cache para cidades diferentes do mesmo país (ex: Nova York e Los Angeles usam o mesmo cache de USD).
- **Feriados são cacheados por país.**
- **Histórico de consultas** guarda apenas `tipo_busca` (CEP ou IP) e `valor_busca` — decisão consciente de não duplicar dados de localização já resolvidos (evita violação de 3ª Forma Normal), mesmo sabendo que tabelas de log às vezes desnormalizam de propósito.
- **Clima e horário não são persistidos** — são dados de passagem, mudam rápido demais para valer a pena cachear.

## Estrutura de pacotes

```
src/main/java/
  model/
    LocalInfo.java
    dto/
      IpApiResponse.java
      BrasilApiCepResponse.java
  service/
    GeoLocationService.java
  db/            (ainda vazio — persistência a implementar)
  Main.java
```

## Como rodar

1. Clonar o repositório e abrir no IntelliJ como projeto Maven.
2. Ter o MySQL instalado localmente (Windows) e o banco `api_dashboard` criado (script SQL em desenvolvimento).
3. Rodar `Main.java` — o programa pede um CEP ou IP no console e imprime o resultado.

## Roadmap

1. Implementar `WeatherService`, `CurrencyService`, `HolidayService` seguindo o mesmo padrão do `GeoLocationService`.
2. Implementar a camada `db` (JDBC) para histórico e cache.
3. Criar um Orchestrator que chama os services em paralelo (`CompletableFuture`) e agrega a resposta.
4. Construir um front mínimo (HTML + CSS + JS) para exibir o resultado.