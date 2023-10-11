package TeamProject.TeamProjectWeb.controller.RestApi;

import TeamProject.TeamProjectWeb.domain.Access;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.MemberDTO;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/members", produces = "application/json")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberRepository memberRepository;

    // 특정 ID의 회원 정보를 조회하는 API 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        // MemberRepository 를 사용하여 해당 ID의 회원 정보를 조회
        MemberDTO dto = memberRepository.findOneDTO(id);
        // 조회된 회원 정보가 없을 경우 404 응답 반환
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        // 조회된 회원 정보를 200 응답과 함께 반환
        return ResponseEntity.ok(dto);
    }

    // 학번으로 해당 학번의 모든 회원 정보를 조회하는 API 엔드포인트
    @GetMapping("/byStudentId/{studentId}")
    public ResponseEntity<List<MemberDTO>> getAllMembersByStudentId(@PathVariable int studentId) {
        // MemberRepository를 사용하여 해당 학번의 모든 회원 정보를 조회
        List<Member> members = memberRepository.findAllByStudentId(studentId);
        // 조회된 회원 정보가 없을 경우 404 응답 반환
        if (members.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<MemberDTO> memberDTOList = ConvertDTO.convertMemberList(members);
        // 조회된 회원 정보를 200 응답과 함께 반환
        return ResponseEntity.ok(memberDTOList);
    }


    // 로그인 아이디로 회원 정보를 조회하는 API 엔드포인트
    @GetMapping("/byLoginId/{loginId}")
    public ResponseEntity<MemberDTO> getMemberByLoginId(@PathVariable String loginId) {
        // MemberRepository를 사용하여 해당 로그인 아이디의 회원 정보를 조회
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Optional<MemberDTO> optionalDTO = ConvertDTO.convertMember(optionalMember);
        // 조회된 회원 정보가 없을 경우 404 응답 반환
        if (optionalMember.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // 조회된 회원 정보를 200 응답과 함께 반환
        return ResponseEntity.ok(optionalDTO.get());
    }

    //  이메일로 회원 정보를 조회하는 API 엔드포인트
    @GetMapping("/byEmail/{email}")
    public ResponseEntity<MemberDTO> getMemberByEmail(@PathVariable String email) {
        // MemberRepository를 사용하여 해당 이메일의 회원 정보를 조회
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        // 조회된 회원 정보가 없을 경우 404 응답 반환
        if (optionalMember.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MemberDTO dto = ConvertDTO.convertMember(optionalMember.get());
        // 조회된 회원 정보를 200 응답과 함께 반환
        return ResponseEntity.ok(dto);
    }


    // 모든 회원 정보를 조회하는 API 엔드포인트
    @GetMapping
    public List<MemberDTO> getAllMembers() {
        // MemberRepository 를 사용하여 모든 회원 정보를 조회
        return ConvertDTO.convertMemberList(memberRepository.findAll());
    }

    // 회원을 생성하는 API 엔드포인트
    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@RequestBody Member member) {
        // MemberRepository 를 사용하여 회원 정보를 저장
        memberRepository.save(member);
        // 저장된 회원 정보를 200 응답과 함께 반환
        return ResponseEntity.ok(ConvertDTO.convertMember(member));
    }

    // ID를 사용하여 회원 정보를 수정하는 API 엔드포인트
    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable Long id, @RequestBody MemberDTO updatedMemberDTO) {
        // 해당 ID로 회원을 찾아옵니다.
        Member existingMember = memberRepository.findOne(id);
        if (existingMember == null) {
            return ResponseEntity.notFound().build();
        }

        // 업데이트된 회원 정보를 기존 회원 정보에 복사하거나 업데이트합니다.
        existingMember.setName(updatedMemberDTO.getName());
        existingMember.setSchoolName(updatedMemberDTO.getSchoolName());
        existingMember.setDepartment(updatedMemberDTO.getDepartment());
        existingMember.setAccess(updatedMemberDTO.getAccess());
        existingMember.setLoginId(updatedMemberDTO.getLoginId());
        existingMember.setPassword(updatedMemberDTO.getPassword());
        existingMember.setEmail(updatedMemberDTO.getEmail());

            // 업데이트된 회원 정보를 저장합니다.
        memberRepository.save(existingMember);

        // 업데이트된 회원 정보를 반환합니다.
        return ResponseEntity.ok(updatedMemberDTO);
    }

    // 특정 ID의 회원 정보를 삭제하는 API 엔드포인트
    @DeleteMapping("/{id}")
    @Transactional
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