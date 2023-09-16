package TeamProject.TeamProjectWeb.controller;

import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String homeLogin(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                            Model model) {

        // 세션에 회원 데이터가 없으면 main
        if (loginMember == null) {
            // loggedIn 값을 false로 모델에 추가
            model.addAttribute("loggedIn", false);
            return "main";
        }

        // 세션이 유지되면 loginMain으로 이동
        model.addAttribute("member", loginMember);
        // loggedIn 값을 true로 모델에 추가
        model.addAttribute("loggedIn", true);
        return "loginMain";
    }
}



/*
/@GetMapping("/")
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
        */