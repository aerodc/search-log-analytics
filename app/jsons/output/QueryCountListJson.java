package jsons.output;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QueryCountListJson {

    @JsonProperty(value = "queries")
    private List<QueryCountJson> queryCountJsons;

    public QueryCountListJson(List<QueryCountJson> queryCountJsons) {
        this.queryCountJsons = queryCountJsons;
    }
}
