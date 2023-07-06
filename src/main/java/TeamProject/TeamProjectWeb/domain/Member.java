package TeamProject.TeamProjectWeb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

// DB 연결시 필요한 코드들과 어노테이션 작성 필요
// 열거형으로 정의된 부분 미정의


//@Entity
@Getter
@Setter
@Table(name = "member")
public class Member { // 회원 클래스
//    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // @GeneratedValue : 자동 생성 => 시퀀스 값 같은
    @Column(name = "member_id")
    private Long id; // 임의로 사용할 키값
    @Column(name = "student_id")
    private int studentId; // 학번
    private String name; // 회원 이름
    @Enumerated(EnumType.STRING) // 데이터값을 int가 아닌 String으로 나오게 함
    private Access access; // 주어질 권한
    @Enumerated(EnumType.STRING) // 데이터값을 int가 아닌 String으로 나오게 함
    private Login login; // 주어질 권한
    @OneToMany(mappedBy = "member") // mappedBy : 연관관계 주인이 누구인지 상태 테이블 속성이름으로 명시해줌
    private List<Board> board = new ArrayList<>();
    @OneToMany(mappedBy = "member") // mappedBy : 연관관계 주인이 누구인지 상태 테이블 속성이름으로 명시해줌
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<TimeSchedule> timetable = new ArrayList<>(); // 한 명의 사용자는 여러 시간표를 가질 수 있음
    @OneToMany(mappedBy = "member")
    private List<Notification> notifications = new ArrayList<>(); // 한 명의 사용자는 여러 알림을 받음

    /*
    //@NotNull
    private Long id;    // 관리자 관리용 db저장 아이디
    @NotEmpty
    private String LoginId;     // 사용자가 로그인하는 아이디
    @NotEmpty
    private String password;  // 비밀번호
    @NotEmpty
    private String name;    // 유저지정이름 ( 또는 진짜 이름 )

    *//*@Email
    private String email;*//*


    private List<Board> board;      // 작성한 게시물 ?
    private List<Comment> comments; // 작성한 댓글 ?
    private List<TimeSchedule> timetable; // 본인의 시간표 ( null 이어도 작동이 되어야 함 )
    private List<Notification> notifications;   // 알림
    */
}
