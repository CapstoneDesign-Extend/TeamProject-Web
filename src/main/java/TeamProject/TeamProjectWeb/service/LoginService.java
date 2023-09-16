package TeamProject.TeamProjectWeb.service;


import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.repository.LoginRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;

    // 주어진 로그인 아이디로 회원을 조회하는 메서드
    public Optional<Member> findByLoginId(String loginId) {
        return loginRepository.findByLoginId(loginId);
    }

    // 주어진 로그인 아이디와 비밀번호로 로그인을 시도하는 메서드
    public Optional<Member> login(String loginId, String password) {
        return loginRepository.login(loginId, password);
    }
}




    /*private final MemberRepository memberRepository;

    public Optional<Member> login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password));
    }*/


