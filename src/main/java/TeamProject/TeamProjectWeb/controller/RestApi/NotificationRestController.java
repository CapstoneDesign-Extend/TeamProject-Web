package TeamProject.TeamProjectWeb.controller.RestApi;


import TeamProject.TeamProjectWeb.domain.Notification;
import TeamProject.TeamProjectWeb.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationRestController {

    private final NotificationRepository notificationRepository;

    // 특정 회원의 알림 목록을 조회하는 API 엔드포인트
    @GetMapping("/{memberId}")
    public List<Notification> getNotificationsByMemberId(@PathVariable Long memberId) {
        // 주어진 memberId로 특정 회원의 알림 목록을 조회함
        // notificationRepository의 findByMemberId 메소드를 호출하여 해당 회원의 알림 목록을 반환함
        return notificationRepository.findByMemberId(memberId);
    }

    // 알림을 생성하는 API 엔드포인트
    @PostMapping("/{memberId}")
    public ResponseEntity<Notification> createNotification(@PathVariable Long memberId,
                                                           @RequestBody String content) {
        // 주어진 memberId와 content를 이용하여 새로운 알림을 생성함
        // notificationRepository의 saveNotification 메소드를 호출하여 새로운 알림을 데이터베이스에 저장함
        Notification notification = notificationRepository.saveNotification(memberId, content);
        // 생성된 알림 정보를 ResponseEntity로 포장하여 반환함
        return ResponseEntity.ok(notification);
    }

    // 알림을 삭제하는 API 엔드포인트
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        // 주어진 notificationId에 해당하는 알림을 조회함
        Notification notification = notificationRepository.findById(notificationId);
        if (notification == null) {
            // 주어진 notificationId에 해당하는 알림이 없는 경우 404 Not Found 상태 코드를 반환함
            return ResponseEntity.notFound().build();
        }
        // 주어진 notificationId에 해당하는 알림을 notificationRepository의 delete 메소드를 호출하여 데이터베이스에서 삭제함
        notificationRepository.delete(notification);
        // 삭제 성공 시 204 No Content 상태 코드를 반환함
        return ResponseEntity.noContent().build();
    }
}