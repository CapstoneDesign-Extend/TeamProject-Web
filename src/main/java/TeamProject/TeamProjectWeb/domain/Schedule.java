package TeamProject.TeamProjectWeb.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Schedule {
    static final int MON = 0;
    static final int TUE = 1;
    static final int WED = 2;
    static final int THU = 3;
    static final int FRI = 4;
    static final int SAT = 5;
    static final int SUN = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 => 시퀀스
    @Column(name = "schedule_id")
    private Long id;

    private String classTitle="";

    private String classPlace="";

    private String professorName="";

    private int day = 0;
    private int startTime;
    private int endTime;

    @ManyToOne(fetch=FetchType.LAZY) // fetch=FetchType.LAZY : 지연 로딩으로 실시간 업로딩 되는 것을 막음
    @JoinColumn(name = "member_id") // 외래키 => 조인할 속성 이름
    @JsonBackReference // Board엔티티를 직렬화할 때 연관된 엔티티 클래스의 정보는 직렬화하지 않도록 하여 순환 참조로 인한 무한루프 방지
    private Member member; // 해당 멤버의 시퀀스 넘버를 사용할 거임

    public Schedule() {
        this.startTime = 0; // 기본 값을 0으로 설정
        this.endTime = 0;   // 기본 값을 0으로 설정
    }


}
