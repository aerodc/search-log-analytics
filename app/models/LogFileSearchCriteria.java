package models;

/**
 *  LogFileSearchCriteria represents a given dateQuery param,
 *  the name of the file to search in,
 *  and whether it is a full file search.
 */
public class LogFileSearchCriteria {

    private String logFilePathName;

    private boolean isPartial;

    private String dateQuery;

    public LogFileSearchCriteria(String logFilePathName, boolean isPartial, String dateQuery) {
        this.logFilePathName = logFilePathName;
        this.isPartial = isPartial;
        this.dateQuery = dateQuery;
    }

    public String getLogFilePathName() {
        return logFilePathName;
    }

    public boolean isPartial() {
        return isPartial;
    }

    public String getDateQuery() {
        return dateQuery;
    }
}
