package TeamProject.TeamProjectWeb.controller;

import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import TeamProject.TeamProjectWeb.domain.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("loggedInMember")
    public Member loggedInMember(HttpSession session) {
        return (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
    }

    @ModelAttribute("loggedIn")
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SessionConst.LOGIN_MEMBER) != null;
    }
}
