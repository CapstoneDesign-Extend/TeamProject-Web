package TeamProject.TeamProjectWeb.controller.RestApi;

import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.domain.Schedule;
import TeamProject.TeamProjectWeb.domain.Time;
import TeamProject.TeamProjectWeb.dto.AndroidScheduleDTO;
import TeamProject.TeamProjectWeb.dto.ScheduleDTO;
import TeamProject.TeamProjectWeb.repository.AndroidScheduleRepository;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import TeamProject.TeamProjectWeb.repository.ScheduleRepository;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/schedules", produces = "application/json")
@RequiredArgsConstructor
public class ScheduleRestController {

    private final AndroidScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/createByMemberId/{memberId}")
    public ResponseEntity<AndroidScheduleDTO> createScheduleByMemberId(@PathVariable Long memberId, @RequestBody AndroidScheduleDTO scheduleDTO) {
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        Schedule schedule = new Schedule();
        // DTO에서 Schedule 엔터티로 데이터를 옮기기
        schedule.setClassTitle(scheduleDTO.getClassTitle());
        schedule.setClassPlace(scheduleDTO.getClassPlace());
        schedule.setProfessorName(scheduleDTO.getProfessorName());
        schedule.setDay(scheduleDTO.getDay());
        // DTO의 Time 객체에서 Entity의 int로 변환
        schedule.setStartTime(convertTimeToInt(scheduleDTO.getStartTime()));
        schedule.setEndTime(convertTimeToInt(scheduleDTO.getEndTime()));

        schedule.setMember(member);  // 멤버를 스케줄에 연결
        scheduleRepository.save(schedule);  // 스케줄 저장

        return ResponseEntity.ok(ConvertDTO.convertSchedule(schedule));
    }
    @GetMapping("/findByMemberId/{memberId}")
    public ResponseEntity<AndroidScheduleDTO> getScheduleByMemberId(@PathVariable Long memberId) {
        Schedule schedule = scheduleRepository.findOneByMemberId(memberId);
        if (schedule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ConvertDTO.convertSchedule(schedule));
    }
    @PutMapping("/updateByMemberId/{memberId}")
    public ResponseEntity<AndroidScheduleDTO> updateScheduleByMemberId(@PathVariable Long memberId, @RequestBody AndroidScheduleDTO updatedScheduleDTO) {
        Schedule existingSchedule = scheduleRepository.findOneByMemberId(memberId);
        if (existingSchedule == null) {
            return ResponseEntity.notFound().build();
        }

        // 이 부분에 스케줄의 속성 업데이트 로직을 추가
        existingSchedule.setClassTitle(updatedScheduleDTO.getClassTitle());
        existingSchedule.setClassPlace(updatedScheduleDTO.getClassPlace());
        existingSchedule.setProfessorName(updatedScheduleDTO.getProfessorName());
        existingSchedule.setDay(updatedScheduleDTO.getDay());
        // DTO의 Time 객체에서 Entity의 int로 변환하는 로직
        existingSchedule.setStartTime(convertTimeToInt(updatedScheduleDTO.getStartTime()));
        existingSchedule.setEndTime(convertTimeToInt(updatedScheduleDTO.getEndTime()));

        scheduleRepository.save(existingSchedule);

        // 업데이트된 스케줄을 DTO로 변환하여 반환
        return ResponseEntity.ok(ConvertDTO.convertSchedule(existingSchedule));
    }
    // Time 객체를 4자리 정수로 변환하는 메서드
    private int convertTimeToInt(Time time) {
        return time.getHour() * 100 + time.getMinute();
    }
    @DeleteMapping("/deleteByMemberId/{memberId}")
    public ResponseEntity<Void> deleteScheduleByMemberId(@PathVariable Long memberId) {
        Schedule schedule = scheduleRepository.findOneByMemberId(memberId);
        if (schedule == null) {
            return ResponseEntity.notFound().build();
        }
        scheduleRepository.deleteByMemberId(memberId);
        return ResponseEntity.noContent().build();
    }
}
