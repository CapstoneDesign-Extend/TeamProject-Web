package TeamProject.TeamProjectWeb.service;

import TeamProject.TeamProjectWeb.domain.Schedule;
import TeamProject.TeamProjectWeb.domain.Time;
import TeamProject.TeamProjectWeb.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // 조회 시 readOnly = true 해당 속성을 주면 최적화됨
//@AllArgsConstructor // 현재 클래스가 가지고 있는 필드를 가지고 생성자를 만들어줌
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public void createScheduleWithTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        Time startTime = new Time(hourStart, minuteStart);
        Time endTime = new Time(hourEnd, minuteEnd);

        Schedule schedule = new Schedule();
        schedule.setStartTime(startTime.toInt());
        schedule.setEndTime(endTime.toInt());

        scheduleRepository.save(schedule);
    }

    public Time convertIntToTime(int timeValue) {
        Time time = new Time();
        time.fromInt(timeValue);
        return time;
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule addSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
}
