package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testMemberTest(){
        Member member = new Member();
        member.setName("memberA");
        member.setStudentId(1234);
        Member saveMember = memberRepository.save(member);


        Member findMember = memberRepository.findByStudentId(saveMember.getStudentId());

        Assertions.assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(saveMember.getName());
        Assertions.assertThat(findMember).isEqualTo(saveMember);

    }

    @Test
    @Transactional
    @Rollback(value = false)
    void testLoginSuccess() {
        // Given
        Member member = new Member();
        member.setLoginId("user");
        member.setName("user");
        member.setPassword("1234");
        memberRepository.save(member);
        String id = member.getLoginId();
        String pwd = member.getPassword();

        // When
        boolean result = memberRepository.login(id, pwd);

        // Then
        System.out.println("result = " + result);
        assertTrue(result); // 로그인 성공이므로 true를 기대함
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void testLoginFailure() {
        // Given
//        String loginId = "user456";
//        String loginPwd = "wrong_password";
        Member member = new Member();
        member.setLoginId("user");
        member.setName("user");
        member.setPassword("1234");
        memberRepository.save(member);
        String id = member.getLoginId();
        String pwd = member.getPassword();

        // When
        boolean result = memberRepository.login(id, pwd);

        // Then
        assertFalse(result); // 로그인 실패이므로 false를 기대함
    }

}