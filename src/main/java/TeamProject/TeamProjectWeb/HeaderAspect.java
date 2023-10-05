package TeamProject.TeamProjectWeb;

import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Aspect
@Component
public class HeaderAspect {

    @Before("execution(* TeamProject.TeamProjectWeb.controller..*(..)) && args(model, ..)")
    public void addHeader(Model model) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        boolean loggedIn = checkLoggedIn(request);
        model.addAttribute("loggedIn", loggedIn);
    }

    public static String timeFriendly(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        long minutesDifference = ChronoUnit.MINUTES.between(dateTime, now);
        long hoursDifference = ChronoUnit.HOURS.between(dateTime, now);
        long daysDifference = ChronoUnit.DAYS.between(dateTime, now);

        if (minutesDifference < 60) {
            return minutesDifference + "분 전";
        } else if (hoursDifference < 24) {
            return hoursDifference + "시간 전";
        } else if (daysDifference < 7) {
            return daysDifference + "일 전";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
            return dateTime.format(formatter);
        }
    }


     //로그인 여부를 확인하는 메서드
    private boolean checkLoggedIn(HttpServletRequest request) {
        // 세션을 가져옵니다. 세션이 없으면 새로 생성하지 않도록 false로 설정합니다.
        HttpSession session = request.getSession(false);

        // 세션에서 로그인 여부를 확인합니다.
        if (session != null) {
            // 세션에서 로그인 정보를 가져옵니다. 여기서 "loggedInUser"는 세션 속성의 이름입니다.
            Object loggedInUser = session.getAttribute("loggedInUser");

            // 만약 로그인 정보가 존재한다면, 로그인 상태로 간주합니다.
            if (loggedInUser != null) {
                return true;
            }
        }

        // 세션 또는 로그인 정보가 없으면 로그인되지 않은 상태로 간주합니다.
        return false;
    }
}
