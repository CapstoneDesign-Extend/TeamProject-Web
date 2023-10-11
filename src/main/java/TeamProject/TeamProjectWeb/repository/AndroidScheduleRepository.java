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
    public List<Schedule> saveAll(List<Schedule> schedules, Long memberId) {
        // Remove existing schedules for the member
        deleteByMemberId(memberId);

        // Save new schedules and return them
        for (Schedule schedule : schedules) {
            if (schedule.getId() == null) {
                em.persist(schedule);
            } else {
                schedule = em.merge(schedule);
            }
        }
        return schedules;
    }

    @Transactional
    public Schedule findOne(Long id) {
        return em.find(Schedule.class, id);
    }

    public List<Schedule> findAll() {
        return em.createQuery("select s from Schedule s", Schedule.class)
                .getResultList();
    }

    @Transactional
    public void deleteByMemberId(Long memberId) {
        List<Schedule> schedules = findByMemberId(memberId);
        for (Schedule schedule : schedules) {
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
