package TeamProject.TeamProjectWeb.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
    private String classTitle;
    private String classPlace;
    private String professorName;
    private int day;
    private int hourStart;
    private int minuteStart;
    private int hourEnd;
    private int minuteEnd;
}
