# Golden Raspberry Awards - Backend

Este projeto é uma API Spring Boot responsável por calcular os produtores com os menores e maiores intervalos entre vitórias do Golden Raspberry Awards, a partir de uma lista de filmes fornecida em arquivo CSV.

## Requisitos

- Java 17 ou superior
- Maven 3.8 ou superior

> Dica: verifique as versões instaladas executando `java -version` e `mvn -version` no terminal.

## Configuração do projeto

1. Clone este repositório e acesse a pasta raiz.
2. Certifique-se de que o Maven consegue baixar as dependências executando o comando abaixo na primeira vez que rodar o projeto (necessita de acesso à internet):
   ```bash
   mvn dependency:resolve
   ```

Os dados utilizados pelos serviços estão no arquivo `src/main/resources/movielist.csv`. Caso queira testar com outro conjunto de dados, substitua este arquivo por um CSV no mesmo formato ou ajuste o caminho nas propriedades da aplicação.

## Executando a aplicação

Para iniciar a API com o Spring Boot em modo de desenvolvimento, execute:

```bash
mvn spring-boot:run
```

Após o processo de build, a aplicação estará disponível em `http://localhost:8080`.

### Endpoints principais

- `GET /api/movies` – Retorna o resumo com os produtores que possuem os menores e maiores intervalos entre vitórias.

## Rodando os testes automatizados

Para executar toda a suíte de testes (unitários e de integração), utilize:

```bash
mvn test
```

O Maven compilará o projeto, executará os testes e exibirá um relatório com o status de cada caso de teste.

## Executando verificações completas (opcional)

Para garantir que o projeto está íntegro, você pode executar a sequência completa de validações:

```bash
mvn clean verify
```

O comando acima realiza limpeza, compilação, execução de testes e qualquer verificação adicional definida no `pom.xml`.

## Estrutura do projeto

```
.
├── README.md
└── golden-raspberry-backend
    ├── pom.xml
    └── src
        ├── main
        │   ├── java/com/example/goldenraspberrybackend
        │   └── resources
        └── test
            └── java/com/example/goldenraspberrybackend
```

- `src/main/java`: código-fonte da aplicação Spring Boot.
- `src/main/resources`: arquivos de configuração e dados (CSV).
- `src/test/java`: testes automatizados.
