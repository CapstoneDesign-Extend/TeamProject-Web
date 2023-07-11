package TeamProject.TeamProjectWeb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
@Table(name = "time_schedule")
public class Timetable { // 시간표
    @Id
    @GeneratedValue // 자동 생성 => 시퀀스
    @Column(name = "time_id")
    private Long id;
    @Column(name = "every_day")
    private String day; // 요일
    private int schedule_year; // 연도
    private int semester; // 학기 => 1학기 or 2학기
    private String schedule; // 일정 => 저장할 스케쥴
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // fetch=FetchType.LAZY : 지연 로딩으로 실시간 업로딩 되는 것을 막음, cascade : 한 번의 조인
    @JoinColumn(name = "member_id") // 외래키 설정
    private Member member; // 한 명의 사용자는 여러 시간표를 가질 수 있음

    //-- 연관관계 편의 메소드 --//

}
