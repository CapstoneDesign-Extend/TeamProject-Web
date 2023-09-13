/*package TeamProject.TeamProjectWeb.controller;

import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")//쿠키가 없어도 익셉션을 발생시키지 않음 (로그인 안해도 홈에 올수있어야하니까)
    public String homeLogin(//@CookieValue(name="memberId", required = false) Long memberId,
                            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                            //이미 로그인 된 사용자를 찾을 때 사용. 세션을 생성하진않음
                            Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}*/



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