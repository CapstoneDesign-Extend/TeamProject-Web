package TeamProject.TeamProjectWeb.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// 열거형으로 정의된 부분 미정의
@Data
public class Board {
    //@NotNull
    private Long id;
    private String title;
    private String content;
    private Member member;
    private int viewCount; // 조회수 ?
    private LocalDateTime finalDate;
    private List<Comment> comment;
}
