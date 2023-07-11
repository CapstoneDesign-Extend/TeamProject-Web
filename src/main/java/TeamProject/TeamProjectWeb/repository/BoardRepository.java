package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 자동으로 스프링 bean이 사용됨
@RequiredArgsConstructor // private final 변수을 생성자 만들어줌
public class BoardRepository {

    @PersistenceContext // EntityManager를 주입받기 위해 사용
    private final EntityManager em;

    public void save(Board board){ //-- 게시글 저장 --//
        em.persist(board); // 영속성 컨텍스트에 저장
    }
    public Board findOne(Long id){ //-- 해당 id로 board(게시글)를 찾아줌 --//
        return em.find(Board.class, id);
    }
    public List<Board> findAll(){ //-- 저장된 게시글을 리스트 형식으로 찾음 --//
        // JPA는 객체를 대상으로 쿼리문을 작성 => 메소드 인자 중 두 번째 인자가 타입을 나타냄
        List<Board> result = em.createQuery("select b from Board b", Board.class)
                .getResultList();
        return result;
    }
    public void delete(Board board) {
        em.remove(board);
    }
    public List<Board> findByBoardId(String title){ //-- 제목으로 게시글 검색 --//
        return em.createQuery("select b from Board b where b.title=:title", Board.class)
                .setParameter("title", title)
                .getResultList();
    }
}
