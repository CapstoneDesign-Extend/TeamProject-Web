package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LoginRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Optional<Member> findByLoginId(String loginId) {
        try {
            // 주어진 로그인 아이디로 회원을 조회하는 JPQL 쿼리
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.loginId = :loginId", Member.class)
                    .setParameter("loginId", loginId);
            Member member = query.getSingleResult();
            return Optional.ofNullable(member);
        } catch (NoResultException e) {
            // 조회 결과가 없으면 Optional.empty() 반환
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Member> login(String loginId, String password) {
        try {
            // 주어진 로그인 아이디와 비밀번호로 회원을 조회하는 JPQL 쿼리
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m WHERE m.loginId = :loginId AND m.password = :password", Member.class)
                    .setParameter("loginId", loginId)
                    .setParameter("password", password);
            Member member = query.getSingleResult();
            return Optional.ofNullable(member);
        } catch (NoResultException e) {
            // 조회 결과가 없으면 Optional.empty() 반환
            return Optional.empty();
        }
    }
}