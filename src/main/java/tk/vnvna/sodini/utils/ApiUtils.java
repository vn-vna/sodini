package tk.vnvna.sodini.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;

public class ApiUtils {
  public static void getTrendingAnimes() {
    try (Client client = ClientBuilder.newClient()) {
      var response = client.target("https://kitsu.io/api/edge/trending/anime")
          .request(MediaType.APPLICATION_JSON)
          .get();

    }
  }
}
