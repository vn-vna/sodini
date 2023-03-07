
package tk.vnvna.sodini.utils.schemas.kitsune;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "en",
  "en_jp",
  "ja_jp",
  "en_us"
})
@Generated("jsonschema2pojo")
public class Titles {

  @JsonProperty("en")
  private String en;
  @JsonProperty("en_jp")
  private String enJp;
  @JsonProperty("ja_jp")
  private String jaJp;
  @JsonProperty("en_us")
  private String enUs;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
