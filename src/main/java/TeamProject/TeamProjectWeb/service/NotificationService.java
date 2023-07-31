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

    /**
     * 알림 생성 메소드
     *
     * @param memberId 알림을 받을 회원의 ID
     * @param content  알림 내용
     */
    public void createNotification(Long memberId, String content) {
        // 1. 외부에서 알림을 저장해 달라 신호를 보내면 알림 생성 -> member id값과 같이 옴
        notificationRepository.saveNotification(memberId, content);
    }

    /**
     * 회원에게 저장된 알림 조회 메소드
     *
     * @param memberId 조회할 회원의 ID
     * @return 해당 회원에게 저장된 알림 리스트
     */
    @Transactional(readOnly = true)
    public List<Notification> findNotificationIsMember(Long memberId) {
        // 2. 해당 member id를 갖는 회원에게 알림을 조회함
        return notificationRepository.findByMemberId(memberId);
    }

    /**
     * 알림 삭제 메소드
     *
     * @param notification 삭제할 알림 객체
     */
    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    /**
     * 알림 ID로 특정 알림 조회 메소드
     *
     * @param id 조회할 알림의 ID
     * @return 해당 ID의 알림 객체
     */
    @Transactional(readOnly = true)
    public Notification findById(Long id) {
        return notificationRepository.findById(id);
    }
}
