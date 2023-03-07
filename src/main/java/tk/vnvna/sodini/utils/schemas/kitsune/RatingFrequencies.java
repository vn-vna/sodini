package tk.vnvna.sodini.utils.schemas.kitsune;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatingFrequencies {

  @JsonProperty("2")
  private String _2Star;
  @JsonProperty("3")
  private String _3Star;
  @JsonProperty("4")
  private String _4Star;
  @JsonProperty("5")
  private String _5Star;
  @JsonProperty("6")
  private String _6Star;
  @JsonProperty("7")
  private String _7Star;
  @JsonProperty("8")
  private String _8Star;
  @JsonProperty("9")
  private String _9Star;
  @JsonProperty("10")
  private String _10Star;
  @JsonProperty("11")
  private String _11Star;
  @JsonProperty("12")
  private String _12Star;
  @JsonProperty("13")
  private String _13Star;
  @JsonProperty("14")
  private String _14Star;
  @JsonProperty("15")
  private String _15Star;
  @JsonProperty("16")
  private String _16Star;
  @JsonProperty("17")
  private String _17Star;
  @JsonProperty("18")
  private String _18Star;
  @JsonProperty("19")
  private String _19Star;
  @JsonProperty("20")
  private String _20Star;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
