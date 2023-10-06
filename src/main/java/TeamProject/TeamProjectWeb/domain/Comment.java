package TeamProject.TeamProjectWeb.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Data
public class Comment { // 댓글 클래스
    @Id
    @GeneratedValue//(strategy = GenerationType.IDENTITY) // 자동 생성 => 시퀀스
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY) // cascade = CascadeType.PERSIST : Comment 테이블을 persist 할 때 Border 테이블도 같이 해줌
    @JoinColumn(name = "board_id") // 게시판 테이블에 PK와 연결해줌
    @JsonIgnore  // 이 엔티티를 직렬화해서 반환할 때, Board 는 제외(순환참조 오류 방지-땜빵식해결, 원래는 DTO를 만들어서 써야함)
    private Board board; // 게시판 id를 가져오기 위해
    private String content; // 본문
    private LocalDateTime finalDate; // 최종 등록된 날짜
    @Column(name = "click_count")
    private int likeCount; // 좋아요 갯수
    @ManyToOne(fetch=FetchType.LAZY) // 한 회원은 여러 댓글을 달 수 있음
    @JoinColumn(name = "memberId") // 외래키 => 조인할 속성 이름
    //@JsonBackReference // 양방향 연관관계에서 역참조 엔티티의 정보를 직렬화하지 않도록 하기(순환 참조로 인한 무한루프 방지)
    private Member member; // 해당 멤버의 학번을 사용할 거임
    private String author; // 익명 또는 사용자명을 저장, 댓글 표시할때 가져오기위함
    @OneToMany(mappedBy = "comment", orphanRemoval = true)  // comment가 삭제되면 연관된 likes도 함께 삭제
    private List<Like> likes = new ArrayList<>();

    public Comment(int chatCnt, String content, String authorName, LocalDateTime now) {
        this.content = content;
        this.finalDate = now;
        this.author = authorName;
        // chatCnt 파라미터는 Comment 클래스의 필드가 아니기 때문에 사용하지 않았습니다. 필요에 따라 적절하게 사용하실 수 있습니다.
    }

    public Comment() {

    }

    //== 연관관계 메소드 ==//
    public void setBoard(Board board){ //-- 게시글에 댓글 저장 메소드 --//
        this.board = board;
        board.getComments().add(this); // 해당 게시물에 댓글을 저장함
    }
}
