package TeamProject.TeamProjectWeb.repository;


import TeamProject.TeamProjectWeb.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testMemberTest(){
        Member member1 = new Member();
        member1.setName("member1");
        member1.setStudentId(1234);
        member1.setLoginId("aaa");
//        Member member2 = new Member();
//        member2.setName("member2");
//        member2.setStudentId(12345);
//        member2.setLoginId("aaa");

        Member saveMember1 = memberRepository.save(member1);
//        Member saveMember2 = memberRepository.save(member2);


//        Member findMember = memberRepository.findByStudentId(saveMember.getStudentId());
//
//        Assertions.assertThat(findMember.getId()).isEqualTo(saveMember.getId());
//        Assertions.assertThat(findMember.getName()).isEqualTo(saveMember.getName());
//        Assertions.assertThat(findMember).isEqualTo(saveMember);

    }
    @Transactional
    @Rollback(value = false)
    @Test
    void testFindMemberByEmailAndLoginId() {
        // Given
        Member member = new Member();
        member.setName("member1");
        member.setStudentId(1234);
        member.setEmail("test@example.com");
        member.setLoginId("test123");
        member.setPassword("password");
        // ... (다른 필수 정보들을 설정)

        // When
        // 멤버를 저장하고 나서 해당 멤버를 조회
        Member saveMember = memberRepository.save(member);
        String result = memberRepository.findPasswordByEmailAndLoginId(saveMember.getEmail(), saveMember.getLoginId());

        // Then
        assertEquals(result, saveMember.getPassword());
    }

    @Transactional
    @Rollback(value = false)
    @Test
    void testFindMemberByEmailAndLoginId_NotFound() {
        // When
        // 존재하지 않는 멤버를 조회
        String result = memberRepository.findPasswordByEmailAndLoginId("nonexistent@example.com", "nonexistent123");

        // Then
        assertNull(result);
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

//    @Test
//    @Transactional
//    @Rollback(value = false)
//    void testLoginFailure() {
//        // Given
////        String loginId = "user456";
////        String loginPwd = "wrong_password";
//        Member member = new Member();
//        member.setLoginId("user");
//        member.setName("user");
//        member.setPassword("1234");
//        memberRepository.save(member);
//        String id = member.getLoginId();
//        String pwd = member.getPassword();
//
//        // When
//        boolean result = memberRepository.login(id, pwd);
//
//        // Then
//        assertFalse(result); // 로그인 실패이므로 false를 기대함
//    }

}