package TeamProject.TeamProjectWeb.controller;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/")//쿠키가 없어도 익셉션을 발생시키지 않음 (로그인 안해도 홈에 올수있어야하니까)
    public String homeLogin(@CookieValue(name="memberId", required = false) Long memberId, Model model) {
        if (memberId == null) {
            return "home";
        }
        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
