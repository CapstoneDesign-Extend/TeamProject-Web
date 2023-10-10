package TeamProject.TeamProjectWeb.controller.schedule;

import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.domain.Schedule;
import TeamProject.TeamProjectWeb.domain.Time;
import TeamProject.TeamProjectWeb.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import TeamProject.TeamProjectWeb.dto.ScheduleDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/schedules")
public class ScheduleController {


    private final ScheduleService scheduleService;

    @GetMapping
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
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

}
