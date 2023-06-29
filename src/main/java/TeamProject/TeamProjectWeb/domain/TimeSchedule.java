package TeamProject.TeamProjectWeb.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// 전달받은 테이블의 Schedule 의 정의가 미비하여 첨부하지 않았음
@Data
public class TimeSchedule {
    //@NotNull
    private Long id;
    private String day;
    private int year;
    private int semester;
    private Member member;
}
