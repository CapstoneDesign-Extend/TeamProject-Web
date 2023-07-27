package TeamProject.TeamProjectWeb.service;

import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // 조회 시 readOnly = true 해당 속성을 주면 최적화됨
//@AllArgsConstructor // 현재 클래스가 가지고 있는 필드를 가지고 생성자를 만들어줌
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class MemberService {

    // 회원가입 -> 중복 체크 ok
    // 회원 알림 확인 -> notification 객체 생성?
    // 회원이 작성한 게시글 조회
    // 회원이 작성한 댓글 조회
    // 회원 정보 확인

    private final MemberRepository memberRepository;
//    @Autowired // 클래스 간의 의존관계를 스프링 컨테이너가 자동으로 연결해줌
//    public void setMemberRepository(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }
    /**
     *  회원 가입
     */
    @Transactional // 데이터 변경 시 트렌젝션 안에서 진행됨
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }
    /**
     *  회원 가입 시 예외 처리(중복회원 처리)
     *  join 메소드에서 만약 중복 회원이 있을 시 validateDuplicateMember 메소드에서 예외처리 해줌
     */
    private void validateDuplicateMember(Member member) { // 만약 중복 회원이 있으면 예외 처리
        List<Member> findMembers = memberRepository.findByStudentId(member.getStudentId());
        if(!findMembers.isEmpty()){ // 해당 결과값 문자열 길이가 0이 아니면
            throw new IllegalStateException("이미 존재하는 회원입니다."); // 이미 존재하는 학번임
        }

    }
    /**
     *  회원 id로 조회된 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<Comment> findCommentsByMemberId(Member member){
        List<Comment> comments = memberRepository.findCommentsByMemberId(member.getId());

        return comments;
    }

    /**
     *  회원 전체 조회 - 관리자 권한
     */
    @Transactional(readOnly = true)
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     *  회원 단권 조회 - 관리자 권한
     */
    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}