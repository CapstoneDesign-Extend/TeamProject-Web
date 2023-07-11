package TeamProject.TeamProjectWeb.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter @Setter
public class Comment { // 댓글 클래스
    @Id
    @GeneratedValue // 자동 생성 => 시퀀스
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL) // cascade = CascadeType.ALL : Comment 테이블을 persist 할 때 Border 테이블도 같이 해줌
    @JoinColumn(name = "border_id") // 게시판 테이블에 PK와 연결해줌
    private Board board; // 게시판 id를 가져오기 위해
    private String content; // 본문
    private LocalDateTime finalDate; // 최종 등록된 날짜
    @Column(name = "click_count")
    private int count; // 좋아요 갯수
    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL) // 한 회원은 여러 댓글을 달 수 있음
    @JoinColumn(name = "memberId") // 외래키 => 조인할 속성 이름
    private Member member; // 해당 멤버의 학번을 사용할 거임

    //== 연관관계 메소드 ==//
    public void setBoard(Board board){
        this.board = board;
        board.getComments().add(this);
    }
}
