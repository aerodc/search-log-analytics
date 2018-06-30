package jsons.output;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountJson {

    @JsonProperty(value = "count")
    private long count;

    public CountJson(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}
