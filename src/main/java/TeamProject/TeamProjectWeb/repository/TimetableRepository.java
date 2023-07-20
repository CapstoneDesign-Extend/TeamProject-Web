package TeamProject.TeamProjectWeb.repository;


import TeamProject.TeamProjectWeb.domain.Timetable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 자동으로 스프링 bean으로 사용됨
@RequiredArgsConstructor
public class TimetableRepository {

    // 1. 시간표 생성 -> 연도와 학기는 필수 속성임
    // 2. 시간표 수정 -> 연도와 학기를 키값으로 찾아서 수정함
    // 3. 시간표 검색 -> 전체 검색으로 개별 검색은 없음
    // 4. 시간표 삭제 -> 연도와 학기를 키로 삭제함

    @PersistenceContext // EntityManager를 주입받기 위해 사용
    private final EntityManager em;

    /**
     * 시간표 저장
     * @param timetable 저장할 시간표 객체
     */
    public void save(Timetable timetable) { // 시간표 db에 저장
        em.persist(timetable);
    }

    /**
     * 시간표 수정
     * @param timetable 수정할 시간표 객체
     */
    public void update(Timetable timetable) {
        // id값으로 변경된 데이터가 있는지 확인한 후 있으면 병합
        em.merge(timetable); // 엔티티의 변경을 추적하고, 변경 내용을 데이터베이스에 반영
    }

    /**
     * 시간표 삭제
     * @param year 연도
     * @param semester 학기
     */
    public void delete(int year, int semester) {
        Timetable timetable = em.createQuery("SELECT t FROM Timetable t WHERE t.schedule_year = :year AND t.semester = :semester", Timetable.class)
                .setParameter("year", year)
                .setParameter("semester", semester)
                .getSingleResult(); // 조회 결과가 없거나, 여러 개의 결과가 있는 경우에는 예외 발생
        if (timetable != null) {
            em.remove(timetable);
        }
    }

    /**
     * 모든 시간표 조회
     * @return 모든 시간표 객체의 리스트
     */
    public List<Timetable> findAll() {
        return em.createQuery("SELECT t FROM Timetable t", Timetable.class)
                .getResultList();
    }

    /**
     * 연도(year)와 학기(semester)로 시간표 조회
     * @param year 연도
     * @param semester 학기
     * @return 조회된 시간표 객체 (없을 경우 null 반환)
     */
    public Timetable findByYearAndSemester(int year, int semester) {
        try {
            return em.createQuery("SELECT t FROM Timetable t WHERE t.schedule_year = :year AND t.semester = :semester", Timetable.class)
                    .setParameter("year", year)
                    .setParameter("semester", semester)
                    .getSingleResult(); // 조회 결과가 없거나, 여러 개의 결과가 있는 경우에는 예외 발생
        } catch (NoResultException e) {
            return null;
        }
    }

}

