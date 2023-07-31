package TeamProject.TeamProjectWeb.controller.notification;

import TeamProject.TeamProjectWeb.domain.Notification;
import TeamProject.TeamProjectWeb.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 생성 폼을 보여줍니다.
    @GetMapping("/create")
    public String createForm() {
        return "notification/createForm";
    }

    // 알림을 생성합니다.
    @PostMapping("/create")
    public String create(@RequestParam("memberId") Long memberId, @RequestParam("content") String content) {
        // 알림을 생성하고 저장합니다.
        notificationService.createNotification(memberId, content);
        return "redirect:/";
    }

    // 특정 회원의 알림 목록을 조회합니다.
    @GetMapping("/{memberId}/list")
    public String list(@PathVariable Long memberId, Model model) {
        // 특정 회원의 알림 목록을 조회하여 모델에 담습니다.
        model.addAttribute("notifications", notificationService.findNotificationIsMember(memberId));
        return "notification/notificationList";
    }

    // 알림을 삭제합니다.
    @PostMapping("/{notificationId}/delete")
    public String delete(@PathVariable Long notificationId) {
        // 알림 ID를 이용하여 알림을 삭제합니다.
        Notification notification = notificationService.findById(notificationId);
        if (notification != null) {
            notificationService.deleteNotification(notification);
        }
        return "redirect:/";
    }
}
