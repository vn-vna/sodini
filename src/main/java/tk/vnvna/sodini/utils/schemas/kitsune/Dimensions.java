
package tk.vnvna.sodini.utils.schemas.kitsune;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dimensions {

    @JsonProperty("tiny")
    private Dimension tiny;
    @JsonProperty("large")
    private Dimension large;
    @JsonProperty("small")
    private Dimension small;
    @JsonProperty("medium")
    private Dimension medium;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
