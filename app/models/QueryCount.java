package models;

import jsons.output.QueryCountJson;

import java.util.*;

/**
 *  Data structures used to get the top N query
 *  A Hashmap with query as key, and count as value
 *  A Heap always keep the top searched query at the top
 */
public class QueryCount {

    private HashMap<String, Integer> queryCountMap;

    private PriorityQueue<QueryCountJson> queryCountHeap;

    public QueryCount(List<String> queryList) {
        buildQueryCountMap(queryList);
        buildQueryCountHeap();
    }

    private void buildQueryCountMap(List<String> queryList) {
        this.queryCountMap = new HashMap<>();
        for (String query : queryList) {
            if (queryCountMap.containsKey(query)) {
                queryCountMap.put(query, queryCountMap.get(query) + 1);
            } else {
                queryCountMap.put(query, 1);
            }
        }
    }

    private void buildQueryCountHeap() {
        this.queryCountHeap = new PriorityQueue<>((o1, o2) -> (-1) * (int) (o1.getCount() - o2.getCount()));
        for (Map.Entry<String, Integer> entry : this.queryCountMap.entrySet()) {
            QueryCountJson queryCountJson = new QueryCountJson(entry.getKey(), entry.getValue());
            this.queryCountHeap.add(queryCountJson);
        }
    }

    public List<QueryCountJson> getTopQueryCounts(int n) {
        List<QueryCountJson> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(this.queryCountHeap.poll());
        }
        return result;
    }
}
