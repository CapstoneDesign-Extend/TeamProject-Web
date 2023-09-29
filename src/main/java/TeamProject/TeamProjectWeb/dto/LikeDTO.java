package TeamProject.TeamProjectWeb.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private Long likeId;
    private Long memberId;
    private Long boardId;
    private Long commentId;
}

