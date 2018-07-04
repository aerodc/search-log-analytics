package services;

import com.typesafe.config.Config;
import jsons.output.QueryCountJson;
import models.LogFileSearchCriteria;
import models.QueryCount;
import transformers.DateQueryParamToPartialFileNames;
import utils.FileUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Service which is responsible for getting the query counts and Top query counts
 */
@Singleton
public class QueryService {

    private final DateQueryParamToPartialFileNames dateQueryParamToFileNames;
    private final FileUtils fileUtils;

    @Inject
    public QueryService(FileUtils fileUtils, Config config) {
        this.fileUtils = fileUtils;
        this.dateQueryParamToFileNames = new DateQueryParamToPartialFileNames(fileUtils, config.getInt("split.log.hour.range"));
    }

    public long getCount(String dateQuery) throws IOException {

        List<String> queries = getQueryLines(dateQuery);
        return queries.stream().distinct().count();
    }

    public List<QueryCountJson> getQueries(String dateQuery, int size) throws IOException {

        List<String> queries = getQueryLines(dateQuery);

        QueryCount queryCount = new QueryCount(queries);

        return queryCount.getTopQueryCounts(size);
    }

    private List<String> getQueryLines(String dateQuery) throws IOException {
        List<LogFileSearchCriteria> logFileSearchCriteriaList = dateQueryParamToFileNames.apply(dateQuery);
        List<String> queries = new ArrayList<>();
        for (LogFileSearchCriteria logFileSearchCriteria : logFileSearchCriteriaList) {
            if (logFileSearchCriteria.isPartial()) {
                queries.addAll(this.fileUtils.getLinesBeginWith(logFileSearchCriteria.getLogFilePathName(), logFileSearchCriteria.getDateQuery()));
            } else {
                queries.addAll(this.fileUtils.getLines(logFileSearchCriteria.getLogFilePathName()));
            }
        }
        return queries;
    }
}
