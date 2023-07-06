package TeamProject.TeamProjectWeb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 열거형으로 정의된 부분 미정의
//@Entity
@Getter
@Setter
@Table(name = "border")
public class Board { // 게시판 클래스
//    @Id
    @GeneratedValue // 자동 생성 => 시퀀스
    @Column(name = "border_id")
    private Long id;
    private String title; // 제목
    private String content; // 본문
    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL) // fetch=FetchType.LAZY : 지연 로딩으로 실시간 업로딩 되는 것을 막음
    @JoinColumn(name = "member_id") // 외래키 => 조인할 속성 이름
    private Member member; // 해당 멤버의 학번을 사용할 거임
    @Column(name = "view_count")
    private int viewcnt; // 조회수
    private LocalDateTime finalDate; // 최종 등록된 날짜
    @Enumerated(EnumType.STRING) // 데이터값을 int가 아닌 String으로 나오게 함
    private Kind kind; // 게시판 종류
    @OneToMany(mappedBy = "board") // mappedBy : 연관관계 주인이 누구인지 상태 테이블 속성이름으로 명시해줌
    private List<Comment> comments = new ArrayList<>();


    /*//@NotNull
    private Long id;
    private String title;
    private String content;
    private Member member;
    private int viewCount; // 조회수 ?
    private LocalDateTime finalDate;
    private List<Comment> comment;*/
}
