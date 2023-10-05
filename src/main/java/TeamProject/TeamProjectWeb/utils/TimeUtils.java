package TeamProject.TeamProjectWeb.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    public static String timeFriendly(LocalDateTime dateTime) {
    LocalDateTime now = LocalDateTime.now();
    long minutesDifference = ChronoUnit.MINUTES.between(dateTime, now);
    long hoursDifference = ChronoUnit.HOURS.between(dateTime, now);
    long daysDifference = ChronoUnit.DAYS.between(dateTime, now);

        if (minutesDifference < 60) {
            return minutesDifference + "분 전";
        } else if (hoursDifference < 24) {
            return hoursDifference + "시간 전";
        } else if (daysDifference < 7) {
            return daysDifference + "일 전";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
            return dateTime.format(formatter);
        }
    }
}
