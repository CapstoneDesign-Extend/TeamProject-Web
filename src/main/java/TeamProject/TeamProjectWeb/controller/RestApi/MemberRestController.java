package TeamProject.TeamProjectWeb.controller.RestApi;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberRepository memberRepository;

    // 특정 ID의 회원 정보를 조회하는 API 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        // MemberRepository 를 사용하여 해당 ID의 회원 정보를 조회
        Member member = memberRepository.findOne(id);
        // 조회된 회원 정보가 없을 경우 404 응답 반환
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        // 조회된 회원 정보를 200 응답과 함께 반환
        return ResponseEntity.ok(member);
    }

    // 모든 회원 정보를 조회하는 API 엔드포인트
    @GetMapping
    public List<Member> getAllMembers() {
        // MemberRepository 를 사용하여 모든 회원 정보를 조회
        return memberRepository.findAll();
    }

    // 회원을 생성하는 API 엔드포인트
    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        // MemberRepository 를 사용하여 회원 정보를 저장
        memberRepository.save(member);
        // 저장된 회원 정보를 200 응답과 함께 반환
        return ResponseEntity.ok(member);
    }

    // 특정 ID의 회원 정보를 삭제하는 API 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberById(@PathVariable Long id) {
        // MemberRepository 를 사용하여 해당 ID의 회원 정보를 조회
        Member member = memberRepository.findOne(id);
        // 조회된 회원 정보가 없을 경우 404 응답 반환
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        // 회원 정보 삭제
        memberRepository.delete(member);
        // 삭제에 성공했으므로 204 응답 반환
        return ResponseEntity.noContent().build();
    }
}