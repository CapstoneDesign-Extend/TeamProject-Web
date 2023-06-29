package TeamProject.TeamProjectWeb.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

// DB 연결시 필요한 코드들과 어노테이션 작성 필요
// 열거형으로 정의된 부분 미정의


@Data
public class Member {

    //@NotNull
    private Long id;    // 관리자 관리용 db저장 아이디
    @NotEmpty
    private String LoginId;     // 사용자가 로그인하는 아이디
    @NotEmpty
    private String password;  // 비밀번호
    @NotEmpty
    private String name;    // 유저지정이름 ( 또는 진짜 이름 )

    /*@Email
    private String email;*/


    private List<Board> board;      // 작성한 게시물 ?
    private List<Comment> comments; // 작성한 댓글 ?
    private List<TimeSchedule> timetable; // 본인의 시간표 ( null 이어도 작동이 되어야 함 )
    private List<Notification> notifications;   // 알림
}
