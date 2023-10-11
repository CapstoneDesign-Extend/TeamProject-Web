package TeamProject.TeamProjectWeb.service;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.domain.Schedule;
import TeamProject.TeamProjectWeb.domain.Time;
import TeamProject.TeamProjectWeb.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // 조회 시 readOnly = true 해당 속성을 주면 최적화됨
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

    public List<Schedule> getSchedulesByMember(Member member) {
        return scheduleRepository.findByMember(member);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule addSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // 수업 정보 수정
    public Schedule editSchedule(Schedule updatedSchedule) {
        Schedule existingSchedule = scheduleRepository.findById(updatedSchedule.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 수업 정보를 찾을 수 없습니다."));

        existingSchedule.setStartTime(updatedSchedule.getStartTime());
        existingSchedule.setEndTime(updatedSchedule.getEndTime());
        existingSchedule.setClassTitle(updatedSchedule.getClassTitle());
        existingSchedule.setClassPlace(updatedSchedule.getClassPlace());
        existingSchedule.setProfessorName(updatedSchedule.getProfessorName());
        existingSchedule.setDay(updatedSchedule.getDay());

        return existingSchedule; // Transactional 어노테이션이 있기 때문에 자동으로 commit 됩니다.
    }

    // 수업 정보 삭제
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
