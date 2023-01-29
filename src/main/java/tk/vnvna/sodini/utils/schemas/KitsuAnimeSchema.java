package tk.vnvna.sodini.utils.schemas;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import lombok.Getter;

import java.util.List;

@Getter
public class KitsuAnimeSchema {
  StdDateFormat createdAt;
  StdDateFormat updatedAt;
  String slug;
  String synopsis;
  Integer coverImageTopOffset;
  AnimeTitle title;
  String canonicalTitle;
  String averageRating;
  Integer userCount;
  Integer favoritesCount;
  StdDateFormat startDate;
  StdDateFormat endDate;
  Integer popularityRank;
  Integer ratingRank;
  String ageRating;
  String ageRatingGuide;
  String subtype;
  String status;
  String tba;

  List<String> abbreviatedTitles;

  public class AnimeTitle {
    public String en;
    public String en_jp;
    public String ja_jp;
  }

  public class PosterImage {
    public String tiny;
    public String small;
    public String medium;
    public String large;
    public String original;

  }
}
