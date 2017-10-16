package steven.hotmail.com.webcrawlerclient;

/**
 * Created by Steven on 10/11/2017.
 */

public class TimeUtil {

    /**
     * Format an integer into a time with the
     * format of H:m S
     * @param seconds
     * @return
     */
    public static String secondsToTime(int seconds)
    {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return timeString;
    }

    public static String secondsToTime(float seconds)
    {
        return secondsToTime((int)seconds);
    }
}
