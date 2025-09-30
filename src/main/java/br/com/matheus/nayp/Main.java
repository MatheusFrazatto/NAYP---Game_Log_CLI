package br.com.matheus.nayp;

import client.RawgApiClient;
import model.Game;
import persistence.DatabaseManager;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void clearConsole() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    private static int getUserInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\n>> ERRO: Entrada Inválida. Por Favor, Digite Apenas um Número.");
                System.out.print("| Tente Novamente: ");
                scanner.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        RawgApiClient apiClient = new RawgApiClient();
        Scanner input = new Scanner(System.in);
        while (true) {
            clearConsole();
            System.out.println("--- MENU GAMELOG ---");
            System.out.println("| [1] Buscar e Salvar um Novo Jogo");
            System.out.println("| [2] Listar Meus Jogos Salvos");
            System.out.println("| [3] Deletar um Jogo");
            System.out.println("| [4] Filtrar Jogos por Status");
            System.out.println("| [5] Mudar Status de um Jogo");
            System.out.println("| [6] Ver Detalhes De Um Jogo Salvo");
            System.out.println("| [0] Sair");
            System.out.print("| Escolha uma Opção: ");
            int opcao = getUserInput(input);
            input.nextLine();
            switch (opcao) {
                case 1:
                    clearConsole();
                    System.out.print("| Digite o Nome do Jogo: ");
                    String searchTerm = input.nextLine();
                    System.out.println(">> Buscando Dados na API...");
                    try {
                        List<Game> games = apiClient.fetchGames(searchTerm);
                        if (games.isEmpty()) {
                            System.out.println(">> Nenhum Jogo Encontrado Com Esse Nome.");
                            break;
                        }
                        System.out.println("--- Jogos Encontrados ---");
                        for (int i = 0; i < games.size(); i++) {
                            System.out.println("[" + (i + 1) + "] - " + games.get(i).getName());
                        }
                        System.out.print("\n| Digite o Número do Jogo que Deseja Salvar (ou 0 para Cancelar): ");
                        int chosenGameNumber = getUserInput(input);
                        input.nextLine();
                        if (chosenGameNumber > 0 && chosenGameNumber <= games.size()) {
                            Game gameSummary = games.get(chosenGameNumber - 1);
                            System.out.println(">> Buscando Detalhes Completos de '" + gameSummary.getName() + "'...");
                            Game gameDetails = apiClient.fetchFullGameDetails(gameSummary.getId());

                            System.out.println("\n| Qual Status Você Quer Dar para '" + gameDetails.getName() + "'?");
                            System.out.println("| [1] Quero Jogar | [2] Jogando | [3] Concluído | [4] Pausado | [5] Abandonado");
                            System.out.print("| Escolha um Status: ");
                            int statusChoice = getUserInput(input);
                            input.nextLine();

                            String statusText = switch (statusChoice) {
                                case 1 -> "Quero Jogar";
                                case 2 -> "Jogando";
                                case 3 -> "Concluído";
                                case 4 -> "Pausado";
                                case 5 -> "Abandonado";
                                default -> "Não Definido";
                            };
                            gameDetails.setStatus(statusText);
                            dbManager.saveGame(gameDetails);
                        } else {
                            System.out.println(">> Operação Cancelada ou Opção Inválida.");
                        }
                    } catch (IOException e) {
                        System.err.println(">> Erro ao Buscar Jogos da API: " + e.getMessage());
                    }
                    break;
                case 2:
                    clearConsole();
                    System.out.println("--- Seus Jogos Salvos ---");
                    List<Game> savedGames = dbManager.getAllGames();
                    if (savedGames.isEmpty()) {
                        System.out.println(">> Você Ainda Não Tem Jogos Salvos.");
                    } else {
                        for (Game game : savedGames) {
                            System.out.println(game.getSummary());
                        }
                    }
                    break;
                case 3:
                    clearConsole();
                    System.out.println("--- Seus Jogos Salvos (para Referência) ---");
                    List<Game> gamesToDelete = dbManager.getAllGames();
                    if (gamesToDelete.isEmpty()) {
                        System.out.println(">> Nenhum Jogo para Deletar.");
                        break;
                    }
                    for (Game game : gamesToDelete) {
                        System.out.println(game.getSummary());
                    }
                    System.out.print("\n| Digite o ID do Jogo que Deseja Remover: ");
                    int idToDelete = getUserInput(input);
                    input.nextLine();
                    dbManager.deleteGame(idToDelete);
                    break;
                case 4:
                    clearConsole();
                    System.out.println("| [1] Quero Jogar | [2] Jogando | [3] Concluído | [4] Pausado | [5] Abandonado");
                    System.out.print("| Escolha um Status para Filtrar: ");
                    int statusChoiceFilter = getUserInput(input);
                    input.nextLine();
                    String statusTextFilter = switch (statusChoiceFilter) {
                        case 1 -> "Quero Jogar";
                        case 2 -> "Jogando";
                        case 3 -> "Concluído";
                        case 4 -> "Pausado";
                        case 5 -> "Abandonado";
                        default -> "";
                    };
                    if (statusTextFilter.isEmpty()) {
                        System.out.println(">> Opção Inválida.");
                        break;
                    }
                    System.out.println("\n--- Jogos com Status: '" + statusTextFilter + "' ---");
                    List<Game> filteredGames = dbManager.getGamesByStatus(statusTextFilter);
                    if (filteredGames.isEmpty()) {
                        System.out.println(">> Nenhum Jogo Encontrado Com Este Status.");
                    } else {
                        for (Game game : filteredGames) {
                            System.out.println(game.getSummary());
                        }
                    }
                    break;
                case 5:
                    clearConsole();
                    System.out.println("--- Seus Jogos Salvos (para Referência) ---");
                    List<Game> gamesToUpdate = dbManager.getAllGames();
                    if (gamesToUpdate.isEmpty()) {
                        System.out.println(">> Nenhum Jogo para Atualizar.");
                        break;
                    }
                    for (Game game : gamesToUpdate) {
                        System.out.println(game.getSummary());
                    }
                    System.out.print("\n| Digite o ID do Jogo que Deseja Alterar o Status: ");
                    int chosenId = getUserInput(input);
                    input.nextLine();
                    System.out.println("\n| Qual o Novo Status para o Jogo de ID " + chosenId + "?");
                    System.out.println("| [1] Quero Jogar | [2] Jogando | [3] Concluído | [4] Pausado | [5] Abandonado");
                    System.out.print("| Escolha um Status: ");
                    int statusRefresh = getUserInput(input);
                    input.nextLine();
                    String statusTextRefresh = switch (statusRefresh) {
                        case 1 -> "Quero Jogar";
                        case 2 -> "Jogando";
                        case 3 -> "Concluído";
                        case 4 -> "Pausado";
                        case 5 -> "Abandonado";
                        default -> "Não Definido";
                    };
                    dbManager.updateGameStatus(chosenId, statusTextRefresh);
                    System.out.println(">> Status Atualizado Com Sucesso!");
                    break;
                case 6:
                    clearConsole();
                    System.out.println("--- Seus Jogos Salvos (para Referência) ---");
                    List<Game> gamesDetails = dbManager.getAllGames();
                    if (gamesDetails.isEmpty()) {
                        System.out.println(">> Nenhum Jogo para Deletar.");
                        break;
                    }
                    for (Game game : gamesDetails) {
                        System.out.println(game.getSummary());
                    }
                    System.out.print("\n| Digite o ID do Jogo que Deseja Deletar: ");
                    int idDetails = getUserInput(input);
                    input.nextLine();
                    Game jogoEncontrado = dbManager.getGameById(idDetails);
                    if (jogoEncontrado != null) {
                        clearConsole();
                        System.out.println("--- Detalhes do Jogo ---");
                        System.out.println(jogoEncontrado);
                    } else {
                        System.out.println("\n>> ERRO: Nenhum jogo encontrado com o ID " + idDetails + ".");
                    }
                    break;
                case 0:
                    System.out.println(">> Até Mais!");
                    return;
                default:
                    System.out.println(">> Opção Inválida. Tente Novamente.");
                    break;
            }
            System.out.println("\n(Pressione Enter para Continuar...)");
            input.nextLine();
        }
    }
}