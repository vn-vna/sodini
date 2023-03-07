package tk.vnvna.sodini.utils.schemas.kitsune;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Relationship {

  @JsonProperty("links")
  private Links links;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
