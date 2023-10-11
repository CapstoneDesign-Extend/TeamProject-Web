package TeamProject.TeamProjectWeb.controller.RestApi;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.domain.Schedule;
import TeamProject.TeamProjectWeb.domain.Time;
import TeamProject.TeamProjectWeb.dto.AndroidScheduleDTO;
import TeamProject.TeamProjectWeb.repository.AndroidScheduleRepository;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/schedules", produces = "application/json")
@RequiredArgsConstructor
public class ScheduleRestController {

    private final AndroidScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/createByMemberId/{memberId}")
    public ResponseEntity<List<AndroidScheduleDTO>> createSchedulesByMemberId(@PathVariable Long memberId, @RequestBody List<AndroidScheduleDTO> scheduleDTOs) {
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        List<Schedule> schedules = new ArrayList<>();
        for(AndroidScheduleDTO dto : scheduleDTOs) {
            Schedule schedule = new Schedule();
            // Convert DTO to Entity
            schedule.setClassTitle(dto.getClassTitle());
            schedule.setClassPlace(dto.getClassPlace());
            schedule.setProfessorName(dto.getProfessorName());
            schedule.setDay(dto.getDay());
            schedule.setStartTime(convertTimeToInt(dto.getStartTime()));
            schedule.setEndTime(convertTimeToInt(dto.getEndTime()));
            schedule.setMember(member);

            schedules.add(schedule);
        }
        scheduleRepository.saveAll(schedules, memberId);

        List<AndroidScheduleDTO> responseDTOs = new ArrayList<>();
        for(Schedule s : schedules) {
            responseDTOs.add(ConvertDTO.convertSchedule(s));
        }

        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/findByMemberId/{memberId}")
    public ResponseEntity<List<AndroidScheduleDTO>> getSchedulesByMemberId(@PathVariable Long memberId) {
        List<Schedule> schedules = scheduleRepository.findByMemberId(memberId);
        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<AndroidScheduleDTO> responseDTOs = new ArrayList<>();
        for(Schedule s : schedules) {
            responseDTOs.add(ConvertDTO.convertSchedule(s));
        }

        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/updateByMemberId/{memberId}")
    public ResponseEntity<List<AndroidScheduleDTO>> updateSchedulesByMemberId(@PathVariable Long memberId, @RequestBody List<AndroidScheduleDTO> updatedScheduleDTOs) {
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        List<Schedule> schedules = new ArrayList<>();
        for(AndroidScheduleDTO dto : updatedScheduleDTOs) {
            Schedule schedule = new Schedule();
            // Convert DTO to Entity
            schedule.setClassTitle(dto.getClassTitle());
            schedule.setClassPlace(dto.getClassPlace());
            schedule.setProfessorName(dto.getProfessorName());
            schedule.setDay(dto.getDay());
            schedule.setStartTime(convertTimeToInt(dto.getStartTime()));
            schedule.setEndTime(convertTimeToInt(dto.getEndTime()));
            schedule.setMember(member);

            schedules.add(schedule);
        }
        scheduleRepository.saveAll(schedules, memberId);

        return ResponseEntity.ok(updatedScheduleDTOs);
    }

    private int convertTimeToInt(Time time) {
        return time.getHour() * 100 + time.getMinute();
    }

    @DeleteMapping("/deleteByMemberId/{memberId}")
    public ResponseEntity<Void> deleteSchedulesByMemberId(@PathVariable Long memberId) {
        scheduleRepository.deleteByMemberId(memberId);
        return ResponseEntity.noContent().build();
    }
}
