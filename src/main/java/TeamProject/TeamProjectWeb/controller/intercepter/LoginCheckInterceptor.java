package TeamProject.TeamProjectWeb.controller.intercepter;


import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// LoginCheckInterceptor 는 인증 체크 기능을 수행하는 인터셉터
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    // preHandle 메서드는 요청 전에 호출되며, 인증 여부를 체크하여 인증되지 않은 사용자의 요청을 처리합니다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();

        // 세션에서 로그인 정보를 확인하여 인증되지 않은 사용자인 경우 로그인 페이지로 리다이렉트합니다.
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            // 로그인 페이지로 리다이렉트하며, 로그인 후 다시 이전 페이지로 돌아올 수 있도록 redirectURL 을 지정합니다.
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false; // 인증되지 않은 사용자의 요청을 처리하지 않고 종료합니다.
        }
        return true; // 인증된 사용자의 요청은 다음 단계로 진행하기 위해 true 를 반환합니다.
    }
}