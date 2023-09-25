package TeamProject.TeamProjectWeb.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;  // comment id
    private Long boardId; // board id
    private String content; // 본문
    private LocalDateTime finalDate; // 최종 등록된 날짜
    private int likeCount; // 좋아요 갯수
    private Long memberId; // user id (db seq)
    private String author; // 익명 또는 사용자명을 저장, 댓글 표시할때 가져오기위함

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDateTime finalDate) {
        this.finalDate = finalDate;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
