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

    @GetMapping("/signup")
    public String signUp(@ModelAttribute("member") Member member) {
        return "members/signup";
    }

    @PostMapping("/signup")
    public String save(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/signup";
        } else {
            memberRepository.save(member);
            return "redirect:/";
        }
    }
}
