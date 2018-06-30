package transformers;

import models.LogFileSearchCriteria;
import utils.DateTimeUtils;
import utils.FileUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static services.LogFileService.DATA_PATH;
import static services.LogFileService.LOG_PARTS_DIR;

/**
 *  Given a datetime param and get a list of partial files which contains the logs in this datetime range.
 */
public class DateQueryParamToPartialFileNames implements Function<String, List<LogFileSearchCriteria>> {

    private final FileUtils fileUtils;
    private final LogLineToPartialFileName logLineToFileName;

    public DateQueryParamToPartialFileNames(FileUtils fileUtils, int hourRange) {
        this.fileUtils = fileUtils;
        logLineToFileName = new LogLineToPartialFileName(hourRange);
    }

    @Override
    public List<LogFileSearchCriteria> apply(String dateQueryParam) {
        List<String> logs = fileUtils.listFileNames(DATA_PATH + LOG_PARTS_DIR);
        String logDir = DATA_PATH + LOG_PARTS_DIR + "/";

        if (DateTimeUtils.isYYYYMMDDHHMM(dateQueryParam)) {
            return logs
                    .stream()
                    .filter(logFileName -> logFileName.equals(logLineToFileName.apply(dateQueryParam + ":00")))
                    .map(log -> new LogFileSearchCriteria(logDir + log, true, dateQueryParam))
                    .collect(Collectors.toList());
        }

        if (DateTimeUtils.isYYYYMMDDHH(dateQueryParam)) {
            return logs
                    .stream()
                    .filter(logFileName -> logFileName.equals(logLineToFileName.apply(dateQueryParam + ":00:00")))
                    .map(log -> new LogFileSearchCriteria(logDir + log, true, dateQueryParam))
                    .collect(Collectors.toList());
        }

        if (DateTimeUtils.isYYYY(dateQueryParam)
                || DateTimeUtils.isYYYYMM(dateQueryParam)
                || DateTimeUtils.isYYYYMMDD(dateQueryParam)) {
            return logs
                    .stream()
                    .filter(logFileName -> logFileName.contains(dateQueryParam))
                    .map(log -> new LogFileSearchCriteria(logDir + log, false, dateQueryParam))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
