package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeUtils {

    public static boolean isYYYY(String date) {
        return isDateValid(date, "yyyy") && date.length() == 4;
    }

    public static boolean isYYYYMM(String date) {
        return isDateValid(date, "yyyy-MM") && date.length() == 7;
    }

    public static boolean isYYYYMMDD(String date) {
        return isDateValid(date, "yyyy-MM-dd") && date.length() == 10;
    }

    public static boolean isYYYYMMDDHH(String date) {
        return isDateValid(date, "yyyy-MM-dd HH") && date.length() == 13;
    }

    public static boolean isYYYYMMDDHHMM(String date) {
        return isDateValid(date, "yyyy-MM-dd HH:mm") && date.length() == 16;
    }

    private static boolean isDateValid(String dateToValidate, String dateFromat) {

        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean checkInputDate(String inputDate) {
        return isYYYY(inputDate) || isYYYYMM(inputDate) || isYYYYMMDD(inputDate) || isYYYYMMDDHH(inputDate) || isYYYYMMDDHHMM(inputDate);
    }
}
