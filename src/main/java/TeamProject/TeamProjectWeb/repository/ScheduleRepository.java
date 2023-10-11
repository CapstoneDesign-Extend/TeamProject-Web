package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByMember(Member member);

}
