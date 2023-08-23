package TeamProject.TeamProjectWeb.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "member")
public class Member { // 회원 클래스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue : 자동 생성 => 시퀀스 값 같은
    @Column(name = "member_id")
    private Long id; // 임의로 사용할 키값
    @Column(name = "student_id")
    private int studentId; // 학번
    private String name; // 회원 이름
    private String schoolName; // 회원 학교
    @Enumerated(EnumType.STRING) // db에 저장할 때, 열거형의 순서(상수)가 아닌 열거형의 이름으로 저장
    private Access access; // 주어질 권한
    @Column(unique=true) //== 유니크 속성을 부여하여 중복으로 아이디를 만드는 것을 방지함 ==/
    private String loginId; // 로그인 시 아이디
    private String password; // 로그인 시 비밀번호
    @Column(unique = true, name = "memberEmail")
    @Email(message = "유효하지 않은 이메일입니다.")
    private String email;
    @OneToMany(mappedBy = "member") // mappedBy : 연관관계 주인이 누구인지 상태 테이블 속성이름으로 명시해줌
    @JsonManagedReference  // Board엔티티를 직렬화할 때 연관된 엔티티 클래스의 정보는 직렬화하지 않도록 하여 순환 참조로 인한 무한루프 방지
    private List<Board> board = new ArrayList<>();
    @OneToMany(mappedBy = "member") // mappedBy : 연관관계 주인이 누구인지 상태 테이블 속성이름으로 명시해줌
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Timetable> timetables = new ArrayList<>(); // 한 명의 사용자는 여러 시간표를 가질 수 있음
    @OneToMany(mappedBy = "member")
    private List<Notification> notifications = new ArrayList<>(); // 한 명의 사용자는 여러 알림을 받음


    //-- 연관관계 편의 메소드 --//
    public void addTimetable(Timetable timetable){ //-- 스케쥴 저장 --//
        this.timetables.add(timetable);
    }
    public void addComment(Comment comment){ //-- 작성된 댓글 저장 --//
        this.comments.add(comment);
    }

    public Member(Long id) {
        this.id = id;
    }
    public Member(){}




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
