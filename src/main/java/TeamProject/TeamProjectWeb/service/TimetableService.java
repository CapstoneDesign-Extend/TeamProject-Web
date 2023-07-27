package TeamProject.TeamProjectWeb.service;

import TeamProject.TeamProjectWeb.domain.Timetable;
import TeamProject.TeamProjectWeb.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class TimetableService {

    private final TimetableRepository timetableRepository;

    public void createTimetable(Timetable timetable) {
        // 시간표 생성 로직
        timetableRepository.save(timetable);
    }

    public void updateTimetable(Timetable timetable) {
        // 시간표 수정 로직
        timetableRepository.update(timetable);
    }

    @Transactional(readOnly = true)
    public List<Timetable> findAllTimetables() {
        // 시간표 조회 로직
        return timetableRepository.findAll();
    }

    public void deleteTimetable(int year, int semester) {
        // 시간표 삭제 로직
        timetableRepository.delete(year, semester);
    }

}

