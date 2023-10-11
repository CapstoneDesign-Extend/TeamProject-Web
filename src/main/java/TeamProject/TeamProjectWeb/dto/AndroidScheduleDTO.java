package TeamProject.TeamProjectWeb.dto;

import TeamProject.TeamProjectWeb.domain.Time;
import lombok.Data;

@Data
public class AndroidScheduleDTO {
    private Long id;  // 시간표 고유 id
    private String classTitle;
    private String classPlace;
    private String professorName;
    private int day;
    private Time startTime;
    private Time endTime;

    private Long memberId;  // 연관된 member의 id
}
