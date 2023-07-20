package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.domain.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 자동으로 스프링 bean으로 사용됨
@RequiredArgsConstructor
public class NotificationRepository {

    // 1. 외부에서 알람을 저장해 달라 신호를 보내면 알람 생성 -> member id값과 같이 옴
    // 2. 해당 member id를 갖는 회원에게 알람을 저장시킴


    @PersistenceContext // EntityManager 팩토리를 주입받기 위해 사용
    private final EntityManager em;

    /**
     * 특정 회원에게 저장된 알림 조회 -> 즉 존재하는 알림 모두 조회
     * @param memberId 조회할 회원의 ID
     * @return 해당 회원에게 저장된 알림 리스트
     */
    public List<Notification> findByMemberId(Long memberId) {
        return em.createQuery("SELECT n FROM Notification n WHERE n.member.id = :memberId", Notification.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public Notification findById(Long id) {
        return em.find(Notification.class, id);
    }

    /**
     * 알림 저장
     * @param notification 저장할 알림 객체
     */
    public void save(Notification notification) {
        em.persist(notification);
    }

    /**
     * 알림 삭제
     * @param notification 삭제할 알림 객체
     */
    public void delete(Notification notification) {
        em.remove(notification);
    }

    /**
     * 특정 회원에게 알림 저장
     * @param memberId 알림을 받을 회원의 ID
     * @param content 알림 내용
     * @return 저장된 알림 객체
     */
    public Notification saveNotification(Long memberId, String content) {
        Notification notification = new Notification();
        notification.setMember(em.getReference(Member.class, memberId));
        notification.setContent(content);
        em.persist(notification);
        return notification;
    }



}

