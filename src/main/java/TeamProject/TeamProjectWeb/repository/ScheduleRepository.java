package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
