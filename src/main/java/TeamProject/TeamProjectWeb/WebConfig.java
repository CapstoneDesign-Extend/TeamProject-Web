package TeamProject.TeamProjectWeb;

import TeamProject.TeamProjectWeb.controller.filter.LogFilter;
import TeamProject.TeamProjectWeb.controller.filter.LoginCheckFilter;
import TeamProject.TeamProjectWeb.controller.intercepter.LogInterceptor;
//import TeamProject.TeamProjectWeb.controller.intercepter.LoginCheckInterceptor;
import TeamProject.TeamProjectWeb.controller.intercepter.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //@Bean 현재 미사용으로 빈에서 해제 재사용 가능성을 위해 코드를 삭제하진 않았음
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    //@Bean 현재 미사용으로 빈에서 해제 재사용 가능성을 위해 코드를 삭제하진 않았음
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())   // 인터셉터 등록
                .order(1)                               // 호출 우선순위 지정, 숫자가 낮을수록 우선호출
                .addPathPatterns("/**")                 // 인터셉터 적용할 URL 패턴 지정
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 인터셉터에서 제외할 패턴을 지정

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(   //화이트리스트 작성
                        "/", "/register/**", "/login/**", "/logout/**", "/api/**", "/register",
                        "/css/**", "/*.ico", "/error /js/**", "fragments/**"//, "templates/**"
                );
    }
}
    /*@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver>
                                             resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }*/

