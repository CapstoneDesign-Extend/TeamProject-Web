package TeamProject.TeamProjectWeb.repository;


import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.MemberDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Slf4j
@Repository // 자동으로 스프링 bean 으로 사용됨
@RequiredArgsConstructor
public class MemberRepository { // repository 패키지는 DB에 접근하는 모든 코드가 모여있음

    @PersistenceContext // EntityManager 를 주입받기 위해 사용
    private EntityManager em;  // EntityManager 는 인터페이스가 아니라 구체적인 클래스이므로
    // final 필드로 주입받는 것이 권장되지 않는다고 함-> 고로 final 제거


    @Transactional
    public Member save(Member member) { //== 멤버 객체를 반환하는 것으로 변경 ==//
        if (member.getId() == null) {
            em.persist(member); // 새로운 엔티티라면 데이터베이스에 삽입
        } else {
            em.merge(member); // 이미 존재하는 엔티티라면 데이터베이스에 업데이트
        }
        return member;
    }

    @Transactional
    public Member findOne(Long id){ //-- 해당 id로 member을 찾아줌 --//
        return em.find(Member.class, id);
    }
    public MemberDTO findOneDTO(Long id) {return em.find(MemberDTO.class, id);}
    @Transactional
    public Optional<Member> findByEmail(String email) {
        try {
            // JPQL 쿼리를 사용하여 해당 loginId를 가진 Member 객체 조회
            // 결과가 없을 경우 NoResultException 예외가 발생하므로 try-catch 블록으로 처리
            return Optional.ofNullable(em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            // 조회 결과가 없을 경우 Optional.empty() 반환
            return Optional.empty();
        }
    }

    public String findPasswordByEmailAndLoginId(String email, String loginId) { //== password를 찾기 위한 sql 쿼리문임 ==//
        // JPQL을 사용하여 조건에 해당하는 Member 찾음
        Member member = em.createQuery(
                        "SELECT m FROM Member m WHERE m.email = :email AND m.loginId = :loginId", Member.class)
                .setParameter("email", email)
                .setParameter("loginId", loginId)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        if (member != null) {
            return member.getPassword(); // 해당 조건에 맞는 member가 있는 경우 password를 반환함
        } else {
            return null; // 해당 조건으로 멤버를 찾을 수 없는 경우
        }
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

    /*public List<Member> findByStudentId(int studentId){ // 학번으로 회원을 찾음 -> 회원을 찾는 건데 list로 반환해버림
        return em.createQuery("select m from Member m where m.studentId=:studentId", Member.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }*/
    @Transactional
    public List<Member> findAllByStudentId(int studentId) {
        // JPQL 쿼리를 사용하여 해당 studentId를 가진 Member 객체들을 조회
        List<Member> members = em.createQuery("SELECT m FROM Member m WHERE m.studentId = :studentId", Member.class)
                .setParameter("studentId", studentId)
                .getResultList();

        return members;
    }

    /*public Optional<Member> findByLoginId(String loginId) { //-- logId 필드로 찾고 해당 결과 반환 --//

        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }*/

    @Transactional
    public Optional<Member> findByLoginId(String loginId) {
        try {
            // JPQL 쿼리를 사용하여 해당 loginId를 가진 Member 객체 조회
            // 결과가 없을 경우 NoResultException 예외가 발생하므로 try-catch 블록으로 처리
            return Optional.ofNullable(em.createQuery("SELECT m FROM Member m WHERE m.loginId = :loginId", Member.class)
                    .setParameter("loginId", loginId)
                    .getSingleResult());
        } catch (NoResultException e) {
            // 조회 결과가 없을 경우 Optional.empty() 반환
            return Optional.empty();
        }
    }


    public List<Comment> findCommentsByMemberId(Long memberId) { // 멤버 ID를 매개변수로 받아 해당 멤버와 연결된 댓글 목록을 조회
        String jpql = "SELECT c FROM Member m JOIN m.comments c WHERE m.id = :memberId";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
        query.setParameter("memberId", memberId);
        return query.getResultList();
    }


    //   login 메소드에서 반환 타입: login 메소드에서 로그인 성공 여부를 반환하고 있습니다. 하지만 반환 타입은 boolean으로 설정되어 있습니다.
    //   이렇게 하면 트랜잭션과의 상호작용에서 문제가 발생할 수 있습니다.
    //   메소드가 boolean을 반환하는 경우, 스프링은 메소드 수행 후에 트랜잭션을 커밋하려고 시도합니다.
    //   따라서 해당 메소드에서 로그인 여부를 반환하는 것보다는 예외를 던져서 트랜잭션 롤백을 유발하는 방법을 고려해야 합니다.
    @Transactional
    public boolean login(String loginId, String password) { // login 메소드 추가
//        try {
        // JPQL 쿼리를 사용하여 해당 loginId와 loginPwd를 가진 Member 객체 조회
        Member member = em.createQuery("SELECT m FROM Member m WHERE m.loginId = :loginId AND m.password = :password", Member.class)
                .setParameter("loginId", loginId)
                .setParameter("password", password)
                .getSingleResult();
        // 조회 결과가 있으면 로그인 성공
        return member != null;
        //로그인 성공: true를 반환
        //로그인 실패: false를 반환
//        } catch (NoResultException e) {
//            // 조회 결과가 없으면 로그인 실패
//            return false;
//        }
        }
    }
