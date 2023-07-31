package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Slf4j
@Repository // 자동으로 스프링 bean으로 사용됨
@RequiredArgsConstructor
public class MemberRepository { // repository 패키지는 DB에 접근하는 모든 코드가 모여있음

    @PersistenceContext // EntityManager를 주입받기 위해 사용
    private final EntityManager em;


    public Member save(Member member) {
        if (member.getId() == null) {
            em.persist(member); // 새로운 엔티티라면 데이터베이스에 삽입
        } else {
            em.merge(member); // 이미 존재하는 엔티티라면 데이터베이스에 업데이트
        }
        return member;
    }
    public Member findOne(Long id){ //-- 해당 id로 member을 찾아줌 --//
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){ //-- 저장된 회원을 리스트 형식으로 찾음 --//
        // JPA는 객체를 대상으로 쿼리문을 작성 => 메소드 인자 중 두 번째 인자가 타입을 나타냄
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        return result;
    }

    public void delete(Member member) { //-- 해당 멤버 삭제 --//
        em.remove(member);
    }
    public List<Member> findByStudentId(int studentId){ // 학번으로 회원을 찾음
        return em.createQuery("select m from Member m where m.studentId=:studentId", Member.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }
    /*public Member findByStudentId(int studentId){ // 학번으로 회원을 찾음
        // JPQL 쿼리를 사용하여 해당 studentId를 가진 Member 객체 조회
        // 결과가 없으면 null을 반환
        List<Member> members = em.createQuery("SELECT m FROM Member m WHERE m.studentId = :studentId", Member.class)
                .setParameter("studentId", studentId)
                .getResultList();

        return members.isEmpty() ? null : members.get(0);
    }*/



    public Optional<Member> findByLoginId(String loginId) { //-- logId 필드로 찾고 해당 결과 반환 --//

        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }
    public List<Comment> findCommentsByMemberId(Long memberId) { // 멤버 ID를 매개변수로 받아 해당 멤버와 연결된 댓글 목록을 조회
        String jpql = "SELECT c FROM Member m JOIN m.comments c WHERE m.id = :memberId";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
        query.setParameter("memberId", memberId);
        return query.getResultList();
    }

    public boolean login(String loginId, String password) { // login 메소드 추가
        try {
            // JPQL 쿼리를 사용하여 해당 loginId와 loginPwd를 가진 Member 객체 조회
            Member member = em.createQuery("SELECT m FROM Member m WHERE m.loginId = :loginId AND m.password = :password", Member.class)
                    .setParameter("loginId", loginId)
                    .setParameter("password", password)
                    .getSingleResult();

            // 조회 결과가 있으면 로그인 성공
            return member != null;
            //로그인 성공: true를 반환
            //로그인 실패: false를 반환
        } catch (NoResultException e) {
            // 조회 결과가 없으면 로그인 실패
            return false;
        }
    }



}



//
//    private static Map<Long, Member> store = new HashMap<>();
//    private static long sequence = 0L; //static 사용했음
//
//    public Member save(Member member) {
//        member.setId(++sequence);
//        log.info("save : member={}", member);
//        store.put(member.getId(), member);
//        return member;
//    }
//
//    public Member findById(Long id) {
//        return store.get(id);
//    }
//
//    public Optional<Member> findByLoginId(String loginId) {
//         /*List<Member> all = findAll();
//         for (Member member : all) {
//             if (member.getLoginId().equals(loginId)) {
//                 return Optional.of(member);
//             }
//         }
//         return Optional.empty();*/
//
//        return findAll().stream()
//                .filter(m -> m.getLongId().equals(loginId))
//                .findFirst();
//    }
//
//    public List<Member> findAll() {
//        return new ArrayList<>(store.values());
//    }
//
//    public void clearStore() {
//        store.clear();
//    }

