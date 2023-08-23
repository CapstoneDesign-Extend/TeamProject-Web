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

    /**
     * 시간표를 생성합니다.
     *
     * @param timetable 생성할 시간표 객체
     */
    public void createTimetable(Timetable timetable) {
        timetableRepository.save(timetable);
    }

    /**
     * 시간표를 수정합니다.
     *
     * @param timetable 수정할 시간표 객체
     */
    public void updateTimetable(Timetable timetable) {
        timetableRepository.update(timetable);
    }

    /**
     * 모든 시간표를 조회합니다.
     *
     * @return 모든 시간표 객체의 리스트
     */
    @Transactional(readOnly = true)
    public List<Timetable> findAllTimetables() {
        return timetableRepository.findAll();
    }

    /**
     * 특정 연도와 학기에 해당하는 시간표를 조회합니다.
     *
     * @param year     연도
     * @param semester 학기
     * @return 조회된 시간표 객체 (없을 경우 null 반환)
     */
    @Transactional(readOnly = true)
    public Timetable findByYearAndSemester(int year, int semester) {
        return timetableRepository.findByYearAndSemester(year, semester);
    }

    /**
     * 시간표를 삭제합니다.
     *
     * @param year     연도
     * @param semester 학기
     */
    public void deleteTimetable(int year, int semester) {
        timetableRepository.delete(year, semester);
    }
}
