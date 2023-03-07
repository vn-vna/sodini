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
public class KitsuTrendingSchema {

  @JsonProperty("data")
  private List<Datum> data = new ArrayList<Datum>();
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
