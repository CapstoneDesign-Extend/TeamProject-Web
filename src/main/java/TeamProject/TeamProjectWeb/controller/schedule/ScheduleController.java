package TeamProject.TeamProjectWeb.controller.schedule;

import TeamProject.TeamProjectWeb.domain.Schedule;
import TeamProject.TeamProjectWeb.domain.Time;
import TeamProject.TeamProjectWeb.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import TeamProject.TeamProjectWeb.dto.ScheduleDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {


    private final ScheduleService scheduleService;

    @GetMapping
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PostMapping
    public Schedule addSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Time startTime = new Time(scheduleDTO.getHourStart(), scheduleDTO.getMinuteStart());
        Time endTime = new Time(scheduleDTO.getHourEnd(), scheduleDTO.getMinuteEnd());

        Schedule schedule = new Schedule();
        schedule.setStartTime(startTime.toInt());
        schedule.setEndTime(endTime.toInt());

        return scheduleService.addSchedule(schedule);
    }
}
