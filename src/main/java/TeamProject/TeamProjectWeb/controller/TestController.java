package TeamProject.TeamProjectWeb.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/mypage/mypage_main")
    public String myPageController(Model model){
        model.addAttribute("loggedIn", true);
        return "/mypage/mypage_main";
    }

    @GetMapping("/personal_info/personal_info_change")
    public String personalInfoChange(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/personal_info_change";
    }

    @GetMapping("/personal_info/pwd_change")
    public String pwdChange(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/pwd_change";
    }

    @GetMapping("/personal_info/email_change")
    public String emailChange(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/email_change";
    }

    @GetMapping("/personal_info/nickname")
    public String nickname(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/nickname";
    }

    @GetMapping("/personal_info/restrict")
    public String restricct(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/restrict";
    }

    @GetMapping("/personal_info/board_maintain")
    public String boardMaintain(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/board_maintain";
    }

    @GetMapping("/personal_info/rules")
    public String rules(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/rules";
    }

    @GetMapping("/personal_info/site_notice")
    public String siteNotice(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/site_notice";
    }

    @GetMapping("/personal_info/serviceagreement")
    public String serviceAgreement(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/serviceagreement";
    }

    @GetMapping("/personal_info/privacy")
    public String privacy(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/privacy";
    }

    @GetMapping("/personal_info/youthpolicy")
    public String youthPolicy(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/youthpolicy";
    }

    @GetMapping("/personal_info/adagreement")
    public String adagreement(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/adagreement";
    }

    @GetMapping("/personal_info/withdraw")
    public String withdraw(Model model){
        model.addAttribute("loggedIn", true);
        return "personal_info/withdraw";
    }

    @GetMapping("/mypage/schedule")
    public String schedule(Model model){
        model.addAttribute("loggedIn", true);
        return "/mypage/schedule";
    }

    @GetMapping("/mypage/alert")
    public String alert(Model model){
        model.addAttribute("loggedIn", true);
        return "/mypage/alert";
    }


}
