package TeamProject.TeamProjectWeb.controller.signup;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberRepository memberRepository;

    // 회원 가입 페이지를 보여주는 GET 요청 핸들러
    @GetMapping("/signup")
    public String signUp(@ModelAttribute("member") Member member) {
        return "members/signup";
    }

    // 회원 가입 처리를 위한 POST 요청 핸들러
    @PostMapping("/signup")
    public String save(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 오류가 있을 경우 다시 회원 가입 페이지로 돌아감
            return "members/signup";
        } else {
            // 유효성 검사를 통과한 경우 회원 정보를 저장하고, 메인 페이지로 리다이렉트
            memberRepository.save(member);
            return "redirect:/";
        }
    }
}
