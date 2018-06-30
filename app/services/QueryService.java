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
        List<LogFileSearchCriteria> logFileSearchCriteriaList = dateQueryParamToFileNames.apply(dateQuery);
        long result = 0;
        for (LogFileSearchCriteria logFileSearchCriteria : logFileSearchCriteriaList) {
            if (logFileSearchCriteria.isPartial()) {
                result = result + fileUtils.countLinesBeginWith(logFileSearchCriteria.getLogFilePathName(), logFileSearchCriteria.getDateQuery());
            } else {
                result = result + fileUtils.countLines(logFileSearchCriteria.getLogFilePathName());
            }
        }
        return result;
    }
    public List<QueryCountJson> getQueries(String dateQuery, int size) throws IOException {
        List<String> queries = new ArrayList<>();
        List<LogFileSearchCriteria> logFileSearchCriteriaList = dateQueryParamToFileNames.apply(dateQuery);

        for (LogFileSearchCriteria logFileSearchCriteria : logFileSearchCriteriaList) {
            if (logFileSearchCriteria.isPartial()) {
                queries.addAll(this.fileUtils.getLinesBeginWith(logFileSearchCriteria.getLogFilePathName(), logFileSearchCriteria.getDateQuery()));
            } else {
                queries.addAll(this.fileUtils.getLines(logFileSearchCriteria.getLogFilePathName()));
            }
        }

        QueryCount queryCount = new QueryCount(queries);

        return queryCount.getTopQueryCounts(size);
    }
}
