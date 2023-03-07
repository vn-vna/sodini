package tk.vnvna.sodini.modules;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.utils.schemas.kitsune.KitsuTrendingSchema;

import javax.json.JsonException;


@AppModule
public class KitsuApiClient {
  public KitsuTrendingSchema getTrendingAnime() {
    try (Client client = ClientBuilder.newClient()) {
      return client
        .target("https://kitsu.io")
        .path("/api/edge/trending/anime")
        .request(MediaType.APPLICATION_JSON)
        .get()
        .readEntity(KitsuTrendingSchema.class);
    } catch (JsonException jex) {
      return null;
    }
  }

  public KitsuTrendingSchema getTrendingManga() {
    try (Client client = ClientBuilder.newClient()) {
      return client
        .target("https://kitsu.io")
        .path("/api/edge/trending/manga")
        .request(MediaType.APPLICATION_JSON)
        .get()
        .readEntity(KitsuTrendingSchema.class);
    } catch (JsonException jex) {
      return null;
    }
  }

}
