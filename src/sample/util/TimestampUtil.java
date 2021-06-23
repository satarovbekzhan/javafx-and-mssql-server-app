package sample.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampUtil {
    public static String getStringFromCurrentDatetime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static Date getCurrentDatetimeFromString(String datetime) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-04-02 02:04:09.000");
    }
}
