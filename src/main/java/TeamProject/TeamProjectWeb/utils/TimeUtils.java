package TeamProject.TeamProjectWeb.utils;

import TeamProject.TeamProjectWeb.domain.Time;

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
//    =============  4자리 정수를 Time(h,m)으로 변환해 반환  =============
    public static Time getTimeFromInt(int time) {
        int hour = time / 100;  // 앞 두 자리는 시간
        int minute = time % 100;  // 뒤 두 자리는 분

        return new Time(hour, minute);
    }
}
