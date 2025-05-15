import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String API_Key = "4e98b5f53624c434011febb5";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> conversoes = new ArrayList<>();

        System.out.println("*******************");
        System.out.println("Porfavor digite seu nome: ");
        String nome = scanner.nextLine().trim().toUpperCase();
        System.out.println("*******************");
        System.out.println("\n**** Olá " + nome + ", Seja bem Vindo(a) ao Conversor de Moedas.****");
        System.out.println("\n*******************");
        while (true) {
            try {
                System.out.println("Porfavor " + nome + ", escolha a moeda de origem para conversão. (BRL, USD, EUR):");
                String moedaOrigem = scanner.nextLine().trim().toUpperCase();
                System.out.println("\n*******************");
                System.out.print("Digite a moeda para a qual que você deseja converter o valor. (ex: BRL, USD, EUR, JPY): ");
                String moedaDestino = scanner.nextLine().trim().toUpperCase();
                System.out.println("\n*******************");
                System.out.print("Digite o valor: ");
                System.out.println("\n*******************");
                double valor = Double.parseDouble(scanner.nextLine().replace(",", "."));


                String endereco = "https://v6.exchangerate-api.com/v6/"  + API_Key + "/latest/" + moedaOrigem;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endereco))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                    JsonObject taxas = json.getAsJsonObject("conversion_rates");

                    if (taxas.has(moedaDestino)) {
                        double taxa = taxas.get(moedaDestino).getAsDouble();
                        double valorConvertido = valor * taxa;

                        System.out.printf("Valor convertido: " + valor + moedaOrigem + " = " + valorConvertido + moedaDestino);
                        conversoes.add("Valor convertido: " + valor + moedaOrigem + " = " + valorConvertido + moedaDestino);
                    } else {
                        System.out.println("Moeda de destino não encontrada.");
                    }
                } else {
                    System.out.println("Erro ao acessar a API. Código HTTP: " + response.statusCode());
                }

            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número, por exemplo: 32.50");
            } catch (IOException | InterruptedException e) {
                System.out.println("Erro na conexão com a API: " + e.getMessage());
            }

            System.out.println("\n*******************");
            System.out.println("Digite 1 para converter novamente ou 2 para sair:");
            String opcao = scanner.nextLine();

            if (opcao.equals("2")) {
                System.out.println("*******************");
                System.out.println("As conversões realizadas foram:" + conversoes);
                System.out.println("\nEncerrando o programa.");

                break;
            }
        }
        scanner.close();
    }
}