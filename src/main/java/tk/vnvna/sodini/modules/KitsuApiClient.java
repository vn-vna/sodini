package tk.vnvna.sodini.modules;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.utils.schemas.kitsune.KitsuneTrendingSchema;

import javax.json.JsonException;


@AppModule
public class KitsuApiClient {
  public KitsuneTrendingSchema getTrendingAnime() {
    try (Client client = ClientBuilder.newClient()) {
      return client
        .target("https://kitsu.io")
        .path("/api/edge/trending/anime")
        .request(MediaType.APPLICATION_JSON)
        .get()
        .readEntity(KitsuneTrendingSchema.class);
    } catch (JsonException jex) {
      return null;
    }
  }

}
