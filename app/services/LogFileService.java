package services;

import play.Environment;
import utils.FileUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

/**
 * Initial Service When Server is starting up:
 * Decompress hn_logs.tsv.gz to hn_logs.tsv
 * Split large file hn_logs.tsv into partial smaller files
 */

@Singleton
public class LogFileService {

    private static final String FILE_HN_LOGS_GZ = "hn_logs.tsv.gz";
    private static final String FILE_HN_LOGS_TSV = "hn_logs.tsv";
    public static final String DATA_PATH = "/data/";
    public static final String LOG_PARTS_DIR = "log_parts";

    @Inject
    public LogFileService(FileUtils fileUtils, Environment environment) throws IOException {
        File hnLogsGzFile = environment.getFile(DATA_PATH + FILE_HN_LOGS_GZ);
        File hnLogsTSVFile = environment.getFile(DATA_PATH + FILE_HN_LOGS_TSV);
        fileUtils.decompressGzip(hnLogsGzFile, hnLogsTSVFile);
        fileUtils.splitFiles(LOG_PARTS_DIR, hnLogsTSVFile);
    }
}


