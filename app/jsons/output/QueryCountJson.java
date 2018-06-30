package jsons.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "query", "count" })
public class QueryCountJson extends CountJson {

    @JsonProperty(value = "query")
    private String query;

    public QueryCountJson(long count) {
        super(count);
    }

    public QueryCountJson(String query, long count) {
        super(count);
        this.query = query;
    }
}
