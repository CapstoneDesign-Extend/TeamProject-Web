package TeamProject.TeamProjectWeb.service;

import TeamProject.TeamProjectWeb.domain.Notification;
import TeamProject.TeamProjectWeb.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(Long memberId, String content) {
        // 1. 외부에서 알림을 저장해 달라 신호를 보내면 알림 생성 -> member id값과 같이 옴
        notificationRepository.saveNotification(memberId, content);
    }

    @Transactional(readOnly = true)
    public List<Notification> findNotificationIsMember(Long memberId) {
        // 2. 해당 member id를 갖는 회원에게 알림을 조회함
        return notificationRepository.findByMemberId(memberId);
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

}
