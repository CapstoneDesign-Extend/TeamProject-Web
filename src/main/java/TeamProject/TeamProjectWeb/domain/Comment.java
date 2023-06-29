package TeamProject.TeamProjectWeb.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    //@NotNull
    private Long id;
    private String content;
    private LocalDateTime finalDate;
    private int count;
    private Member member;
}
