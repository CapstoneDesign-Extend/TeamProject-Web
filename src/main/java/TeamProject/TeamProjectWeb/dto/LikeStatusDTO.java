package TeamProject.TeamProjectWeb.dto;

import lombok.Data;

import java.util.List;

public class LikeStatusDTO {  // 특정 게시글에 "현재 접속자"가 좋아요를 했는지, 그리고 그(특정 게시글에 작성된) 댓글들에 좋아요를 했다면 그 댓글목록을 정의하기 위함
    private boolean isLikedBoard;
    private List<Long> likedCommentIds;

    // lombok이 메서드 이름에서 is를 빼먹고 생성해서 내가 직접 정의함
    public boolean isLikedBoard() {
        return isLikedBoard;
    }

    public void setIsLikedBoard(boolean likedBoard) {
        isLikedBoard = likedBoard;
    }

    public List<Long> getLikedCommentIds() {
        return likedCommentIds;
    }

    public void setLikedCommentIds(List<Long> likedCommentIds) {
        this.likedCommentIds = likedCommentIds;
    }
}
