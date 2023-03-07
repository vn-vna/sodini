package tk.vnvna.sodini.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import tk.vnvna.sodini.utils.schemas.KitsuAnimeTrendingListSchema;

public class ApiUtils {
  public static void getTrendingAnimes() {
    try (Client client = ClientBuilder.newClient()) {
      var response = client
        .target("https://kitsu.io")
        .path("/api/edge/trending/anime")
        .request(MediaType.APPLICATION_JSON)
        .get()
        .readEntity(String.class);

      var data = new ObjectMapper().readValue(response, KitsuAnimeTrendingListSchema.class);

      System.out.println(data);
    } catch (JsonProcessingException e) {
      System.out.println(e);
    }
  }

  public static void main(String[] args) {
    getTrendingAnimes();
  }
}
