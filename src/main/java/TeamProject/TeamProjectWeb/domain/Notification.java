package TeamProject.TeamProjectWeb.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Notification {
    //@NotNull
    private Long id;
    private String content;
    private Member member;
}
