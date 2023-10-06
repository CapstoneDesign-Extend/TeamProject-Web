package TeamProject.TeamProjectWeb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeResponse {
    private boolean liked; // 현재 좋아요 상태
    private int likeCnt;  // 현재 게시물의 총 좋아요 수
}
