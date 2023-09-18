package TeamProject.TeamProjectWeb.controller.signup;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    private HttpSession httpSession;

    // 회원 가입 페이지를 보여주는 GET 요청 핸들러
    @GetMapping("/register")
    public String signUpForm(@ModelAttribute("member") Member member, Model model) {

        // 년도 목록을 생성 (예: 2023부터 1990까지)
        List<Integer> years = new ArrayList<>();
        int currentYear = Year.now().getValue();
        for (int year = currentYear; year >= 1990; year--) {
            years.add(year);
        }

        // "years"와 "year" 변수를 모델에 추가
        model.addAttribute("years", years);
        model.addAttribute("year", years.get(0)); // 기본값 설정
        // "schoolName" 변수를 모델에 추가
        model.addAttribute("schoolName", ""); // 초기값은 빈 문자열로 설정 (또는 기본값으로 설정)

        return "register/register";
    }

    // 회원 목록 페이지를 보여주는 GET 요청 핸들러
    @GetMapping("/list")
    public String listMembers(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "register/list";
    }

    // 사용자가 register.html 에서 데이터를 입력한 후, 이 핸들러를 통해 agree_policy 페이지로 데이터를 전달
    @PostMapping("/agree_policy")
    public String moveToAgreePolicy(@ModelAttribute("member") Member member) {
        // 필요한 로직이 있다면 여기에 추가
        httpSession.setAttribute("tempMember", member);  // 세션에 Member 객체 저장
        return "register/agree_policy"; // agree_policy 페이지로 이동
    }

    @PostMapping("/info_input")
    public String showInfoInputForm(Model model) {
        Member tempMember = (Member) httpSession.getAttribute("tempMember"); // 세션에서 Member 객체 가져오기
        if(tempMember != null) {
            model.addAttribute("member", tempMember);
        } else {
            model.addAttribute("member", new Member());
        }
        return "register/info_input";
    }

        // 회원 가입 처리를 위한 POST 요청 핸들러
    // 이거 근데.. 다음 페이지로 넘겨야하는데 이거 쓰는거 맞나 ? 일단 주석처리해봄;
    @PostMapping("/info_input/signup")
    @Transactional // 오류 해결위해 추가됨::트랜잭션 적용
    public String save(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register/register";
        } else {
            Member tempMember = (Member) httpSession.getAttribute("tempMember");
            if(tempMember != null) {
                member.setSchoolName(tempMember.getSchoolName()); // schoolName 병합
            }
            memberService.join(member);
            return "redirect:/";
        }
    }


    // 사용자가 info_input.html에서 데이터를 입력한 후, 이 핸들러를 통해 데이터를 저장하고 메인 페이지로 리다이렉트
//    @PostMapping("/info_input")
//    public String saveInfo(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "signup/info_input"; // 유효성 검사 오류 시 info_input 페이지로 다시 돌아감
//        } else {
//            memberService.join(member); // Member 객체 저장
//            return "redirect:/"; // 메인 페이지로 리다이렉트
//        }
//    }

}

//    // 회원 가입 처리를 위한 POST 요청 핸들러
//    // 이거 근데.. 다음 페이지로 넘겨야하는데 이거 쓰는거 맞나 ? 일단 주석처리해봄;
//    @PostMapping("/signup")
//    @Transactional // 오류 해결위해 추가됨::트랜잭션 적용
//    public String save(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            // 유효성 검사 오류가 있을 경우 다시 회원 가입 페이지로 돌아감
//            return "signup/signup";
//        } else {
//            // 유효성 검사를 통과한 경우 회원 정보를 저장하고, 메인 페이지로 리다이렉트
//            memberService.join(member);
//            return "redirect:/";
//        }
//    }