package TeamProject.TeamProjectWeb.controller.login;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.LoginForm;
import TeamProject.TeamProjectWeb.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    //private boolean loggedIn; // 로그인 여부를 나타내는 필드

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form, Model model, HttpServletRequest request) {
        // 로그인 여부를 확인하여 loggedIn 변수를 모델에 추가
//        boolean loggedIn = checkLoggedIn(request);
        model.addAttribute("loggedIn", false);

        // 인터셉터에서 설정한 errorMessage 를 가져와 모델에 추가
        if (request.getAttribute("errorMessage") != null) {
            model.addAttribute("errorMessage", request.getAttribute("errorMessage"));
        }

        return "/login/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request,
                        Model model) {

        // 폼 데이터의 유효성 검사 결과가 오류가 있는지 확인합니다.
        if (bindingResult.hasErrors()) {
            // 오류가 있을 경우 다시 로그인 폼으로 이동합니다.
            return "redirect:/login/login";
        }

        // 로그인 서비스를 이용해 로그인을 시도합니다.
        Optional<Member> loginMemberOptional = loginService.login(form.getLoginId(), form.getPassword());

        // 로그인 성공 여부를 확인합니다.
        // 회원 조회에 성공하면 해당 회원 정보가 Optional<Member>로 반환
        if (loginMemberOptional.isPresent()) {
            // 로그인에 성공했다면 해당 회원 정보를 가져옵니다.
            // 해당 회원 정보를 HttpSession 객체에 저장합니다. 이를 통해 로그인 상태를 관리합니다.
            Member loginMember = loginMemberOptional.get();
            // 로그인 성공을 로그로 기록합니다.
            log.info("로그인 성공: memberId={}, loginId={}", loginMember.getId(), loginMember.getLoginId());


            // 로그인 성공 처리: 회원 정보를 세션에 저장합니다.
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
            model.addAttribute("loggedIn", true);
            // 로그인에 성공했을 경우, 사용자는 원래의 페이지(redirectURL)로 리다이렉트
            return "redirect:" + redirectURL;
        } else {
            // 로그인 실패시 오류 메시지를 설정하고 다시 로그인 폼으로 이동합니다.
            log.info("로그인 실패: loginId={}", form.getLoginId());
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "redirect:/login/login";
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestParam("loginId") String loginId,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         Model model) {

        // 로그아웃한 회원의 아이디를 로그로 기록합니다.
        log.info("로그아웃: {}", loginId);

        // 현재 세션을 가져옵니다. (false로 설정하여 세션이 없으면 새로 생성하지 않습니다)
        HttpSession session = request.getSession(false);

        // 세션이 존재하는 경우, 세션을 무효화하여 로그아웃 처리합니다.
        if (session != null) {
            session.invalidate();
        }
        model.addAttribute("loggedIn", false);
        // 로그아웃 처리 후에는 메인 페이지로 리다이렉트합니다.
        return "redirect:/";
    }
}

//    // 로그인 여부를 확인하는 메서드
//    private boolean checkLoggedIn(HttpServletRequest request) {
//        HttpSession session = request.getSession(false); // 세션이 없으면 새로 생성하지 않도록 false로 설정
//        return session != null && session.getAttribute(SessionConst.LOGIN_MEMBER) != null;
//    }
