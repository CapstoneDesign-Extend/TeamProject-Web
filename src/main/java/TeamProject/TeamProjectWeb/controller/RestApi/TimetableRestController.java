package TeamProject.TeamProjectWeb.controller.RestApi;


import TeamProject.TeamProjectWeb.domain.Timetable;
import TeamProject.TeamProjectWeb.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
public class TimetableRestController {

    private final TimetableRepository timetableRepository;

    // 시간표 생성 API 엔드포인트
    @PostMapping
    public ResponseEntity<Timetable> createTimetable(@RequestBody Timetable timetable) {
        // 주어진 시간표 객체를 시간표 저장 메소드를 이용하여 데이터베이스에 저장함
        timetableRepository.save(timetable);
        // 생성된 시간표 정보를 ResponseEntity로 포장하여 반환함
        return ResponseEntity.ok(timetable);
    }

    // 시간표 수정 API 엔드포인트
    @PutMapping("/{year}/{semester}")
    public ResponseEntity<Timetable> updateTimetable(@PathVariable int year,
                                                     @PathVariable int semester,
                                                     @RequestBody Timetable updatedTimetable) {
        // 주어진 연도(year)와 학기(semester)로 해당 시간표를 검색함
        Timetable timetable = timetableRepository.findByYearAndSemester(year, semester);
        if (timetable == null) {
            // 해당 연도와 학기에 해당하는 시간표가 없는 경우 404 Not Found 상태 코드를 반환함
            return ResponseEntity.notFound().build();
        }

        // 변경된 시간표 정보를 기존 시간표 객체에 업데이트함
        timetable.setSchedule_year(updatedTimetable.getSchedule_year());
        timetable.setSemester(updatedTimetable.getSemester());
        // 추가적인 필드 업데이트 등 필요한 로직 작성

        // 변경된 시간표 객체를 시간표 수정 메소드를 이용하여 데이터베이스에 저장함
        timetableRepository.update(timetable);
        // 수정된 시간표 정보를 ResponseEntity로 포장하여 반환함
        return ResponseEntity.ok(timetable);
    }

    // 시간표 삭제 API 엔드포인트
    @DeleteMapping("/{year}/{semester}")
    public ResponseEntity<Void> deleteTimetable(@PathVariable int year, @PathVariable int semester) {
        // 주어진 연도(year)와 학기(semester)로 해당 시간표를 삭제함
        timetableRepository.delete(year, semester);
        // 삭제 성공 시 204 No Content 상태 코드를 반환함
        return ResponseEntity.noContent().build();
    }

    // 모든 시간표 조회 API 엔드포인트
    @GetMapping
    public List<Timetable> getAllTimetables() {
        // 모든 시간표 객체의 리스트를 조회하여 반환함
        return timetableRepository.findAll();
    }
}