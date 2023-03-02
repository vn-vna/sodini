package tk.vnvna.sodini.utils.schemas;

public class KitsuAnimeTrendingListSchema {
  KitsuAnimeAttributeSchema[] data;

  public class AnimeTrendingSchema {
    String id;
    String type;
    AnimeTrendingLinks links;
    KitsuAnimeAttributeSchema attributes;
  }

  public class AnimeTrendingLinks {
    String self;
  }
}
