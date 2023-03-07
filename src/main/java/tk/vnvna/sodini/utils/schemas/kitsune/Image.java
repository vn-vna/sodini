package tk.vnvna.sodini.utils.schemas.kitsune;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image {

  @JsonProperty("tiny")
  private String tiny;
  @JsonProperty("large")
  private String large;
  @JsonProperty("small")
  private String small;
  @JsonProperty("medium")
  private String medium;
  @JsonProperty("original")
  private String original;
  @JsonProperty("meta")
  private Meta meta;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
