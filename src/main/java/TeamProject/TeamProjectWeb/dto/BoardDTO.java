package TeamProject.TeamProjectWeb.dto;

import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.domain.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Data
public class BoardDTO {

    private Long id;
    private String title; // 제목
    private String content; // 본문
    private String author; // 익명 또는 사용자명을 저장, 게시판에 출력할때 가져오기위함

    private Long memberId; // member's id (DB seq)
    private LocalDateTime finalDate; // 최종 등록된 날짜
    private BoardKind boardKind; // 게시판 종류
    private int viewCnt; // 조회수
    private int likeCnt; // 좋아요 개수
    private int chatCnt; // 댓글수

    private List<String> imageURLs; // 이미지 URL 목록

}
