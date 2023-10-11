package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Schedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AndroidScheduleRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Schedule save(Schedule schedule) {
        if (schedule.getId() == null) {
            em.persist(schedule);
        } else {
            schedule = em.merge(schedule);
        }
        return schedule;
    }

    @Transactional
    public Schedule findOne(Long id) {
        return em.find(Schedule.class, id);
    }
    @Transactional
    public Schedule findOneByMemberId(Long memberId) {
        try {
            return em.createQuery("SELECT s FROM Schedule s WHERE s.member.id = :memberId", Schedule.class)
                    .setParameter("memberId", memberId)
                    .setMaxResults(1)  // 한 개의 결과만 가져오기
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;  // 조회된 결과가 없을 경우 null 반환
        }
    }

    public List<Schedule> findAll() {
        return em.createQuery("select s from Schedule s", Schedule.class)
                .getResultList();
    }
    @Transactional
    public void deleteByMemberId(Long memberId) {
        Schedule schedule = findOneByMemberId(memberId);
        if (schedule != null) {
            em.remove(schedule);
        }
    }
    @Transactional
    public void delete(Schedule schedule) {
        em.remove(schedule);
    }

    public List<Schedule> findByMemberId(Long memberId) {
        return em.createQuery("select s from Schedule s where s.member.id = :memberId", Schedule.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public Optional<Schedule> findByClassTitle(String classTitle) {
        try {
            return Optional.ofNullable(
                    em.createQuery("SELECT s FROM Schedule s WHERE s.classTitle = :classTitle", Schedule.class)
                            .setParameter("classTitle", classTitle)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Schedule> findByDay(int day) {
        return em.createQuery("SELECT s FROM Schedule s WHERE s.day = :day", Schedule.class)
                .setParameter("day", day)
                .getResultList();
    }
}
