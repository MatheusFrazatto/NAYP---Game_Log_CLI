package persistence;

import model.Game;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:gamelog.db";

    public DatabaseManager() {
        initDatabase();
    }

    private Game mapResultSetToGame(ResultSet rs) throws SQLException {
        Game game = new Game();
        game.setId(rs.getInt("id"));
        game.setName(rs.getString("name"));
        game.setDescription_raw(rs.getString("description"));
        game.setReleased(rs.getString("released"));
        game.setMetacritic(rs.getInt("metacritic"));
        game.setBackground_image(rs.getString("background_image"));
        game.setStatus(rs.getString("status"));
        return game;
    }

    private void initDatabase() {
        String sqlCreateGames = "CREATE TABLE IF NOT EXISTS games ("
                + " id INTEGER PRIMARY KEY,"
                + " name TEXT NOT NULL,"
                + " released TEXT,"
                + " description TEXT,"
                + " metacritic INTEGER,"
                + " background_image TEXT,"
                + " status TEXT"
                + ");";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateGames);
            System.out.println("Banco de dados inicializado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }

    public void saveGame(Game game) {
        if (gameExists(game.getId())) {
            System.out.println("\n[AVISO] O jogo '" + game.getName() + "' já está na sua coleção e não será salvo novamente.");
            return;
        }
        String sql = "INSERT INTO games (id, name, released, description, metacritic, background_image, status) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, game.getId());
            pstmt.setString(2, game.getName());
            pstmt.setString(3, game.getReleased());
            pstmt.setString(4, game.getDescription_raw());
            pstmt.setInt(5, game.getMetacritic());
            pstmt.setString(6, game.getBackground_image());
            pstmt.setString(7, game.getStatus());
            pstmt.executeUpdate();
            System.out.println("Dados principais de '" + game.getName() + "' salvos com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar o jogo completo: " + e.getMessage());
        }
    }

    public void deleteGame(int id) {
        String sql = "DELETE FROM games WHERE id = ?;";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Jogo com ID " + id + " deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao deletar o jogo: " + e.getMessage());
        }
    }

    public List<Game> getGamesByStatus(String status) {
        String sql = "SELECT * FROM games WHERE status = ?;";
        List<Game> games = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                games.add(mapResultSetToGame(rs));
            }
        } catch (SQLException e) {
            System.err.println(">> Erro Ao Buscar Jogos Por Status: " + e.getMessage());
        }
        return games;
    }

    public void updateGameStatus(int gameId, String newStatus) {
        String sql = "UPDATE games SET status = ? WHERE id = ?;";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, gameId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(">> Erro Ao Atualizar Status do Jogo: " + e.getMessage());
        }
    }

    public Game getGameById(int id) {
        String sql = "SELECT * FROM games WHERE id = ?;";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToGame(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar jogo por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Game> getAllGames() {
        String sql = "SELECT * FROM games;";
        List<Game> games = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                games.add(mapResultSetToGame(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar os jogos: " + e.getMessage());
        }
        return games;
    }

    private boolean gameExists(int gameId) {
        String sql = "SELECT 1 FROM games WHERE id = ? LIMIT 1;";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar a existência do jogo: " + e.getMessage());
        }
        return false;
    }
}