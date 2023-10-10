package TeamProject.TeamProjectWeb.domain;

import lombok.Data;

@Data
public class Time {
    private int hour = 0;
    private int minute = 0;

    public Time() {} // 기본 생성자 추가

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String toFormattedString() {
        return String.format("%02d:%02d", hour, minute);
    }

    public int toInt() {
        return hour * 100 + minute;
    }

    public void fromInt(int timeValue) {
        this.hour = timeValue / 100;
        this.minute = timeValue % 100;
    }
}
