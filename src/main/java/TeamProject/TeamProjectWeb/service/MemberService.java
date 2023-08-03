package TeamProject.TeamProjectWeb.service;


import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 시 readOnly = true 해당 속성을 주면 최적화됨
//@AllArgsConstructor // 현재 클래스가 가지고 있는 필드를 가지고 생성자를 만들어줌
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class MemberService {

    private final MemberRepository memberRepository;
//    @Autowired // 클래스 간의 의존관계를 스프링 컨테이너가 자동으로 연결해줌
//    public void setMemberRepository(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }
//    /**
//     *  회원 가입
//     */
//    @Transactional // 데이터 변경 시 트렌젝션 안에서 진행됨
//    public Long join(Member member){
//        validateDuplicateMember(member); // 중복 회원 검증
//        memberRepository.save(member);
//        return member.getId();
//    }
//    /**
//     *  회원 가입 시 예외 처리(중복회원 처리)
//     *  join 메소드에서 만약 중복 회원이 있을 시 validateDuplicateMember 메소드에서 예외처리 해줌
//     */
//    private void validateDuplicateMember(Member member) { // 만약 중복 회원이 있으면 예외 처리
//        List<Member> findMembers = memberRepository.findByStudentId(member.getStudentId());
//        if(!findMembers.isEmpty()){ // 해당 결과값 문자열 길이가 0이 아니면
//            throw new IllegalStateException("이미 존재하는 회원입니다."); // 이미 존재하는 학번임
//        }
//
//    }

    /*@Transactional // 메소드가 트랜잭션 내에서 실행되도록 보장
    public Member join(Member member) {
        // 로그인 아이디가 이미 존재하는지 확인하여 중복 가입을 방지
        if (isLoginIdExists(member.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 아이디입니다. 다른 아이디를 선택해주세요.");
        }

        // 새로운 멤버의 권한을 기본값으로 설정
        member.setAccess(member.getAccess());

        // save 메소드를 사용하여 멤버 저장
        return memberRepository.save(member);
    }

    private boolean isLoginIdExists(String loginId) {
        // 로그인 아이디가 이미 존재하는지 확인하는 메소드를 리포지토리에 구현합니다.
        // 예를 들어, MemberRepository에 findByLoginId와 같은 메소드를 추가하여 로그인 아이디로 멤버를 찾습니다.
        return memberRepository.findByLoginId(loginId) != null;
    }*/

    @Transactional
    public Member join(Member member) {
        // 로그인 아이디가 이미 존재하는지 확인하여 중복 가입을 방지
        if (isLoginIdExists(member.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 아이디입니다. 다른 아이디를 선택해주세요.");
        }

        // 새로운 멤버의 권한을 기본값으로 설정
        member.setAccess(member.getAccess());

        // save 메소드를 사용하여 멤버 저장
        return memberRepository.save(member);
    }

    private boolean isLoginIdExists(String loginId) {
        // 로그인 아이디가 이미 존재하는지 확인하는 메소드를 리포지토리에 구현합니다.
        // 예를 들어, MemberRepository에 findByLoginId와 같은 메소드를 추가하여 로그인 아이디로 멤버를 찾습니다.
        return memberRepository.findByLoginId(loginId).isPresent();
    }

    /**
     *  회원 전체 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    /**
     *  회원 단권 조회
     */
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}