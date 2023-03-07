
package tk.vnvna.sodini.utils.schemas.kitsune;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attributes {

  @JsonProperty("createdAt")
  private String createdAt;
  @JsonProperty("updatedAt")
  private String updatedAt;
  @JsonProperty("slug")
  private String slug;
  @JsonProperty("synopsis")
  private String synopsis;
  @JsonProperty("description")
  private String description;
  @JsonProperty("coverImageTopOffset")
  private Integer coverImageTopOffset;
  @JsonProperty("titles")
  private Map<String, String> titles;
  @JsonProperty("canonicalTitle")
  private String canonicalTitle;
  @JsonProperty("abbreviatedTitles")
  private List<String> abbreviatedTitles = new ArrayList<String>();
  @JsonProperty("averageRating")
  private String averageRating;
  @JsonProperty("ratingFrequencies")
  private RatingFrequencies ratingFrequencies;
  @JsonProperty("userCount")
  private Integer userCount;
  @JsonProperty("favoritesCount")
  private Integer favoritesCount;
  @JsonProperty("startDate")
  private String startDate;
  @JsonProperty("endDate")
  private String endDate;
  @JsonProperty("nextRelease")
  private Object nextRelease;
  @JsonProperty("popularityRank")
  private Integer popularityRank;
  @JsonProperty("ratingRank")
  private Integer ratingRank;
  @JsonProperty("ageRating")
  private String ageRating;
  @JsonProperty("ageRatingGuide")
  private String ageRatingGuide;
  @JsonProperty("subtype")
  private String subtype;
  @JsonProperty("status")
  private String status;
  @JsonProperty("tba")
  private Object tba;
  @JsonProperty("posterImage")
  private Image posterImage;
  @JsonProperty("coverImage")
  private Image coverImage;
  @JsonProperty("episodeCount")
  private Integer episodeCount;
  @JsonProperty("episodeLength")
  private Integer episodeLength;
  @JsonProperty("totalLength")
  private Integer totalLength;
  @JsonProperty("youtubeVideoId")
  private String youtubeVideoId;
  @JsonProperty("showType")
  private String showType;
  @JsonProperty("chapterCount")
  private Integer chapterCount;
  @JsonProperty("volumeCount")
  private Integer volumeCount;
  @JsonProperty("mangaType")
  private String mangaType;
  @JsonProperty("serialization")
  private String serialization;
  @JsonProperty("nsfw")
  private Boolean nsfw;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
