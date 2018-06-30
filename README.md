### Solution:

#### Indexing:

- Split the large log file into smaller pieces by a specific range of time. Then we could load the smaller file into memory for searching queries.

- e.g. in this case we are given a hn_logs ranges from 2015-08-01 to 2015-08-04, we split the logs by 8 hours.


#### Searching:
- Using Hashmap with query as key, count as value.
- Using Heap (PriorityQueue in Java) to get the top N queries.


#### Running the app:
- `git clone <project>`

- `cd <project>`

- `./sbt clean run`

- Using a GET ping to wake up the server :`curl -k localhost:9000/search-log/ping`

- Waiting a little while for log files processing

- The location of hn_logs are under /data, /data/log_parts


- e.g request:
`curl -k localhost:9000/search-log/1/queries/count/2015-08`

`curl -k localhost:9000/search-log/1/queries/popular/2015?size=3`