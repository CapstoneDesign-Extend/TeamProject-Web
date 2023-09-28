package TeamProject.TeamProjectWeb.dto;

import TeamProject.TeamProjectWeb.domain.Access;
import lombok.Data;

@Data
public class MemberDTO {
    private Long id;
    private Integer studentId; // 학번
    private String name; // 회원 이름
    private String schoolName; // 회원 학교
    private Access access; // 주어질 권한
    private String loginId; // 로그인 시 아이디
    private String password; // 로그인 시 비밀번호
    private String email;
}
