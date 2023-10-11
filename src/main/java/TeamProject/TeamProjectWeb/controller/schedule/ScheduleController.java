package TeamProject.TeamProjectWeb.controller.schedule;

import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.domain.Schedule;
import TeamProject.TeamProjectWeb.domain.Time;
import TeamProject.TeamProjectWeb.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import TeamProject.TeamProjectWeb.dto.ScheduleDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/schedules")
public class ScheduleController {


    private final ScheduleService scheduleService;

    @GetMapping
    public List<Schedule> getAllSchedules(HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loggedInMember == null) {
            return new ArrayList<>(); // 로그인하지 않은 사용자에게는 빈 목록을 반환
        }
        return scheduleService.getSchedulesByMember(loggedInMember);
    }

    @PostMapping
    public Schedule addSchedule(@RequestBody ScheduleDTO scheduleDTO, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Time startTime = new Time(scheduleDTO.getHourStart(), scheduleDTO.getMinuteStart());
        Time endTime = new Time(scheduleDTO.getHourEnd(), scheduleDTO.getMinuteEnd());

        Schedule schedule = new Schedule();
        schedule.setStartTime(startTime.toInt());
        schedule.setEndTime(endTime.toInt());
        schedule.setMember(loggedInMember);

        // 아래 코드 추가
        schedule.setClassTitle(scheduleDTO.getClassTitle());
        schedule.setClassPlace(scheduleDTO.getClassPlace());
        schedule.setProfessorName(scheduleDTO.getProfessorName());
        schedule.setDay(scheduleDTO.getDay());

        System.out.println("hourStart: " + scheduleDTO.getHourStart());
        System.out.println("minuteStart: " + scheduleDTO.getMinuteStart());
        System.out.println("Time toInt: " + startTime.toInt());

        return scheduleService.addSchedule(schedule);
    }

    // 수업 수정
    @PutMapping("/{id}")
    public Schedule editSchedule(@PathVariable Long id, @RequestBody ScheduleDTO scheduleDTO, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Time startTime = new Time(scheduleDTO.getHourStart(), scheduleDTO.getMinuteStart());
        Time endTime = new Time(scheduleDTO.getHourEnd(), scheduleDTO.getMinuteEnd());

        Schedule schedule = new Schedule();
        schedule.setId(id);  // id 설정
        schedule.setStartTime(startTime.toInt());
        schedule.setEndTime(endTime.toInt());
        schedule.setMember(loggedInMember);

        schedule.setClassTitle(scheduleDTO.getClassTitle());
        schedule.setClassPlace(scheduleDTO.getClassPlace());
        schedule.setProfessorName(scheduleDTO.getProfessorName());
        schedule.setDay(scheduleDTO.getDay());

        return scheduleService.editSchedule(schedule);  // Service에 editSchedule 메소드 추가 필요
    }

    // 수업 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);  // Service에 deleteSchedule 메소드 추가 필요
        return ResponseEntity.ok().build();  // 정상적으로 삭제된 경우 200 OK 응답
    }

}
