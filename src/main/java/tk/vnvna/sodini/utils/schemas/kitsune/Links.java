
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "self"
})
@Generated("jsonschema2pojo")
public class Links {

    @JsonProperty("self")
    private String self;

    @JsonProperty("related")
    private String related;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
