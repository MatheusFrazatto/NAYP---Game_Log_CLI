package client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.github.cdimascio.dotenv.Dotenv;
import model.ApiResponse;
import model.Game;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RawgApiClient {
    private final OkHttpClient client;
    private final Gson gson;
    private final String apiKey;
    private static final String BASE_URL = "https://api.rawg.io/api/";

    public RawgApiClient() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("RAWG_API_KEY");
    }

    public List<Game> fetchGames(String searchTerm) throws IOException {
        String url = BASE_URL + "games?key=" + this.apiKey + "&search=" + URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Falha na chamada de busca à API: " + response);
            }
            String jsonBody = response.body().string();
            ApiResponse apiResponse = gson.fromJson(jsonBody, ApiResponse.class);
            return apiResponse.getResults();
        } catch (JsonSyntaxException e) {
            throw new IOException("Erro ao processar o JSON da busca.", e);
        }
    }

    public Game fetchFullGameDetails(int gameId) throws IOException {
        String url = BASE_URL + "games/" + gameId + "?key=" + this.apiKey;
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Falha na chamada de detalhes à API: " + response);
            }
            String jsonBody = response.body().string();
            return gson.fromJson(jsonBody, Game.class);
        } catch (JsonSyntaxException e) {
            throw new IOException("Erro ao processar o JSON da API.", e);
        }
    }
}