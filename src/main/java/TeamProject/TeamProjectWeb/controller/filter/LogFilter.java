package TeamProject.TeamProjectWeb.controller.filter;

import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException
    //HTTP 요청이 오면 doFilter 가 호출된다.
    //ServletRequest request 는 HTTP 요청이 아닌 경우까지 고려해서 만든 인터페이스이다.
    //HTTP를 사용하면 HttpServletRequest httpRequest = (HttpServletRequest) request; 와 같이 다운 케스팅 하면 된다.
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString(); // HTTP 요청을 구분하기 위해 요청당 임의의 uuid 를 생성함
        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response);
            // 다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출한다.
            // 만약 이 로직을 호출하지 않으면 다음 단계로 진행되지 않는다.
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }
    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}