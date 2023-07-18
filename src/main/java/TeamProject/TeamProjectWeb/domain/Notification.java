package TeamProject.TeamProjectWeb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
@Table(name = "notification")
public class Notification { // 사용자 개인적인 알림 저장
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 => 시퀀스
    @Column(name = "notification_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // fetch=FetchType.LAZY : 지연 로딩으로 실시간 업로딩 되는 것을 막음
    @JoinColumn(name = "memberId")
    private Member member; // 한 명의 사용자는 여러 알림을 받음
    private String content; // 알림 내용
}
