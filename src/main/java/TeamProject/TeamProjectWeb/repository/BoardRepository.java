package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.MainBoardDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 자동으로 스프링 bean으로 사용됨
@RequiredArgsConstructor
public class BoardRepository {
    // 1. 게시글 생성 -> 각각 알맞은 게시판 속성 저장
    // 2. 게시글 수정 -> 해당 권한을 가진 member만 수정 가능
    // 3. 게시글 삭제 -> 해당 member id로 판별
    // 4. 게시글 검색 -> 연관된 제목으로 검색

    @PersistenceContext // EntityManager를 주입받기 위해 사용
    private final EntityManager em;

    @Transactional
    public void save(Board board){ // 게시글 저장
        // Board 엔티티를 생성할 떄, 영속 상태가 아닌 Member 엔티티를 사용중이라면 해당 Member 엔티티를 영속 상태로 만들기
        if (board.getMember() != null && board.getMember().getId() != null){
            board.setMember(em.getReference(Member.class, board.getMember().getId()));
        }
        em.persist(board);
    }

    // 해당 id를 가진 게시글 반환
    public Board findOne(Long id){
        return em.find(Board.class, id);
    }
    // 모든 게시글 리스트 반환
    public List<Board> findAll(){
        // JPA는 객체를 대상으로 쿼리문을 작성 => 메소드 인자 중 두 번째 인자가 타입을 나타냄
        List<Board> result = em.createQuery("select b from Board b order by b.finalDate desc", Board.class)
                .getResultList();
        return result;
    }
    // 해당 boardKind 의 게시글 리스트 반환
    public List<Board> findByBoardKind(BoardKind boardKind) {
        return em.createQuery("select b from Board b where b.boardKind = :boardKind order by b.finalDate desc", Board.class)
                .setParameter("boardKind", boardKind)
                .getResultList();
    }
    // 해당 boardKind 의 게시글을 필요한 만큼(amount)만 반환
    public List<Board> findByBoardKindAmount(BoardKind boardKind, int amount) {
        return em.createQuery("select b from Board b where b.boardKind = :boardKind order by b.finalDate desc", Board.class)
                .setParameter("boardKind", boardKind)
                .setMaxResults(amount)
                .getResultList();
    }
    // 제목으로 검색해 게시글 리스트 반환
    public List<Board> findByTitle(String title) {
        return em.createQuery("select b from Board b where b.title like :title order by b.finalDate desc", Board.class)
                .setParameter("title", "%" + title + "%")
                .getResultList();
    }
    // 제목이나 본문에 해당 키워드를 포함하는 게시글들을 반환
    public List<Board> findByKeyword(String keyword) {
        return em.createQuery("select b from Board b where b.title like :keyword or b.content like :keyword order by b.finalDate desc", Board.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }
    // 특정 boardKind 의 게시글만 키워드로 검색
    public List<Board> findByKeywordKind(String keyword, BoardKind boardKind) {
        return em.createQuery("select b from Board b where (b.title like :keyword or b.content like :keyword) and b.boardKind = :boardKind order by b.finalDate desc", Board.class)
                .setParameter("keyword", "%" + keyword + "%")
                .setParameter("boardKind", boardKind)
                .getResultList();
    }

    public void delete(Board board) {
        em.remove(board);
    }
    public void deleteById(Long id) { // 해당 게시글 id로 삭제함
        Board board = em.find(Board.class, id);
        if (board != null) {
            em.remove(board);
        }
    }

    //페이징 형태로 게시글 리스트 출력 다만 이것은 BoardKind 의 영향없이 모든 게시글을 보여준다
    public List<Board> findBoardListWithPaging(Pageable pageable) {
        return em.createQuery("select b from Board b order by b.finalDate desc", Board.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    //페이징 형태로 게시글 리스트 출력 이 메서드는 BoardKind 의 영향으로 선택한 게시판의 게시물만 보여준다.
    public List<Board> findBoardListByKindWithPaging(BoardKind boardKind, Pageable pageable) {
        return em.createQuery("select b from Board b where b.boardKind = :boardKind order by b.finalDate desc", Board.class)
                .setParameter("boardKind", boardKind)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    // 게시글 수 반환 메서드
    public long count() {
        return em.createQuery("select count(b) from Board b", Long.class)
                .getSingleResult();
    }

    // BoardKind 에 따른 게시글 수 반환 메서드
    public long countByBoardKind(BoardKind boardKind) {
        return em.createQuery("select count(b) from Board b where b.boardKind = :boardKind", Long.class)
                .setParameter("boardKind", boardKind)
                .getSingleResult();
    }

    //이 쿼리는 Board 엔터티의 title과 finalDate만을 선택하여 MainBoardDTO에 할당하고,
    // 최근 게시글을 기준으로 내림차순 정렬하여 결과를 반환
    public List<MainBoardDTO> findRecentBoardsForMainPage(int limit) {
        return em.createQuery("select new TeamProject.TeamProjectWeb.dto.MainBoardDTO(b.title, b.finalDate) from Board b order by b.finalDate desc", MainBoardDTO.class)
                .setMaxResults(limit)
                .getResultList();
    }

    // 이 쿼리는 특정 BoardKind에 따른 Board 엔터티의 title과 finalDate만을 선택하여 MainBoardDTO에 할당하고,
    // 최근 게시글을 기준으로 내림차순 정렬하여 결과를 반환
    public List<MainBoardDTO> findRecentBoardsByKindForMainPage(BoardKind boardKind, int limit) {
        return em.createQuery("select new TeamProject.TeamProjectWeb.dto.MainBoardDTO(b.title, b.finalDate) from Board b where b.boardKind = :boardKind order by b.finalDate desc", MainBoardDTO.class)
                .setParameter("boardKind", boardKind)
                .setMaxResults(limit)
                .getResultList();
    }


}
