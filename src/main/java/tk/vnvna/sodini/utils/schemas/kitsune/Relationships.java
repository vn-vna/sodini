
package tk.vnvna.sodini.utils.schemas.kitsune;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "genres",
    "categories",
    "castings",
    "installments",
    "mappings",
    "reviews",
    "mediaRelationships",
    "characters",
    "staff",
    "productions",
    "quotes",
    "episodes",
    "streamingLinks",
    "animeProductions",
    "animeCharacters",
    "animeStaff"
})
@Generated("jsonschema2pojo")
public class Relationships {

    @JsonProperty("genres")
    private Relationship genres;
    @JsonProperty("categories")
    private Relationship categories;
    @JsonProperty("castings")
    private Relationship castings;
    @JsonProperty("installments")
    private Relationship installments;
    @JsonProperty("mappings")
    private Relationship mappings;
    @JsonProperty("reviews")
    private Relationship reviews;
    @JsonProperty("mediaRelationships")
    private Relationship mediaRelationships;
    @JsonProperty("characters")
    private Relationship characters;
    @JsonProperty("staff")
    private Relationship staff;
    @JsonProperty("productions")
    private Relationship productions;
    @JsonProperty("quotes")
    private Relationship quotes;
    @JsonProperty("episodes")
    private Relationship episodes;
    @JsonProperty("streamingLinks")
    private Relationship streamingLinks;
    @JsonProperty("animeProductions")
    private Relationship animeProductions;
    @JsonProperty("animeCharacters")
    private Relationship animeCharacters;
    @JsonProperty("animeStaff")
    private Relationship animeStaff;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
