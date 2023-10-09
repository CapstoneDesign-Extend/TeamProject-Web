package TeamProject.TeamProjectWeb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentResponse {

    private Long id;
    private Long chatCnt;
    private String content;
    private String authorName;
    private String formattedFinalDate;
    private int likeCount;
}
