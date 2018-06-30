package transformers;

import exceptions.SearchLogException;

import java.util.function.Function;

/**
 *  Given a line of log and get a name of partial file where this line of log should be written in.
 */
public class LogLineToPartialFileName implements Function<String, String> {

    private final int hourRange;

    public LogLineToPartialFileName(int hourRange) {
        this.hourRange = hourRange;
    }

    @Override
    public String apply(String dateTimeString) {
        String[] dateTimeArray = dateTimeString.split(" ");
        if (dateTimeArray.length != 2) {
            throw new SearchLogException("invalid logs");
        }
        String date = dateTimeArray[0];
        String time = dateTimeArray[1];

        String[] timeArray = time.split(":");
        if (timeArray.length != 3) {
            throw new SearchLogException("invalid logs");
        }
        return date + "_part" + Integer.parseInt(timeArray[0]) / hourRange;
    }
}
