package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.BoardDTO;
import TeamProject.TeamProjectWeb.dto.BoardSummaryDTO;
import TeamProject.TeamProjectWeb.dto.MainBoardDTO;
import TeamProject.TeamProjectWeb.utils.BoardRepositoryCustom;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Repository // 자동으로 스프링 bean으로 사용됨
@RequiredArgsConstructor
public class BoardRepository implements BoardRepositoryCustom {

    @PersistenceContext // EntityManager를 주입받기 위해 사용
    private final EntityManager em;

    public Board save(Board board){ // 게시글 저장
//        board.setAttachFile(uploadFile);
        // Board 엔티티를 생성할 떄, 영속 상태가 아닌 Member 엔티티를 사용중이라면 해당 Member 엔티티를 영속 상태로 만들기
        if (board.getMember() != null && board.getMember().getId() != null){
            board.setMember(em.getReference(Member.class, board.getMember().getId()));
        }
        em.persist(board);
        return board;
    }
    public void update(Board board) {
        em.merge(board);
    }

    public Board findById(Long id){
        return em.find(Board.class, id);
    }

    // 해당 id를 가진 게시글을 DTO로 반환
    public BoardDTO findOneDTO(Long id){
        return ConvertDTO.convertBoard(em.find(Board.class, id));
    }

    // 해당 id를 가진 게시글 반환
    public Board findOne(Long id){  // DTO를 반환하도록 변경
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

    @Transactional
    public void delete(Board board) {  // 사용금지:: 엔티티를 직접 사용하고있음
        em.remove(board);
    }

    @Transactional
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

    // 이 쿼리는 Board 엔터티의 title 과 finalDate 만을 선택하여 MainBoardDTO 에 할당하고,
    // 최근 게시글을 기준으로 내림차순 정렬하여 결과를 반환
    public List<MainBoardDTO> findRecentBoardsForMainPage(int limit) {
        return em.createQuery("select new TeamProject.TeamProjectWeb.dto.MainBoardDTO(b.title, b.finalDate) from Board b order by b.finalDate desc", MainBoardDTO.class)
                .setMaxResults(limit)
                .getResultList();
    }

    // 이 쿼리는 특정 BoardKind 에 따른 Board 엔터티의 title 과 finalDate 만을 선택하여 MainBoardDTO 에 할당하고,
    // 최근 게시글을 기준으로 내림차순 정렬하여 결과를 반환
    public List<MainBoardDTO> findRecentBoardsByKindForMainPage(BoardKind boardKind, int limit) {
        return em.createQuery("select new TeamProject.TeamProjectWeb.dto.MainBoardDTO(b.title, b.finalDate) from Board b where b.boardKind = :boardKind order by b.finalDate desc", MainBoardDTO.class)
                .setParameter("boardKind", boardKind)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<MainBoardDTO> findRecentBoardsByKindForDepartmentBoard(BoardKind boardKind, int limit, String schoolName, String department) {
        return em.createQuery("select new TeamProject.TeamProjectWeb.dto.MainBoardDTO(b.title, b.finalDate) from Board b where b.boardKind = :boardKind AND b.member.schoolName = :schoolName AND b.member.department = :department order by b.finalDate desc", MainBoardDTO.class)
                .setParameter("boardKind", boardKind)
                .setParameter("schoolName", schoolName)
                .setParameter("department", department)
                .setMaxResults(limit)
                .getResultList();
    }


    // 게시글 종류와 페이징 정보를 받아서 해당 게시글들과 그에 연관된 댓글을 가져오는 메서드를 정의
    public List<Board> findBoardWithComments(BoardKind boardKind, Pageable pageable) {
        // JPQL 쿼리로, Board에서 comments를 join fetch로 가져옵니다.
        // where 절로 게시글의 종류를 조건으로 하며, finalDate를 기준으로 내림차순으로 정렬합니다.
        return em.createQuery("select b from Board b join fetch b.comments where b.boardKind = :boardKind order by b.finalDate desc", Board.class)
                // JPQL 쿼리의 :boardKind 변수에 실제 boardKind 값을 바인딩합니다.
                .setParameter("boardKind", boardKind)
                // 페이징 처리를 위해 시작 위치를 설정합니다.
                .setFirstResult((int) pageable.getOffset())
                // 한 페이지에 가져올 결과의 수를 설정합니다.
                .setMaxResults(pageable.getPageSize())
                // 쿼리를 실행하고 결과를 리스트로 반환합니다.
                .getResultList();
    }

    // 게시판 종류에 따른 게시글 요약 정보 조회
    @Override
    public Page<BoardSummaryDTO> findSummaryByBoardKind(BoardKind boardKind, Pageable pageable) {
        // JPQL 쿼리를 이용해 게시판 종류에 따른 게시글 정보 및 좋아요와 댓글의 개수를 가져옵니다.
        String jpql = "SELECT new TeamProject.TeamProjectWeb.dto.BoardSummaryDTO(b.id, b.title, b.content, b.finalDate, b.author, " +
                "(SELECT COUNT(l) FROM Like l WHERE l.board = b), " +
                "(SELECT COUNT(c) FROM Comment c WHERE c.board = b)) " +
                "FROM Board b WHERE b.boardKind = :boardKind order by b.finalDate desc";

        // 위에서 정의한 JPQL 쿼리 실행
        List<BoardSummaryDTO> results = em.createQuery(jpql, BoardSummaryDTO.class)
                .setParameter("boardKind", boardKind)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // 페이지네이션을 위한 총 게시글 개수 조회
        long total = em.createQuery("SELECT COUNT(b) FROM Board b WHERE b.boardKind = :boardKind", Long.class)
                .setParameter("boardKind", boardKind)
                .getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    // 게시판 종류, 학과, 학교에 따른 게시글 요약 정보 조회
    public Page<BoardSummaryDTO> findSummaryByBoardKindAndSchoolAndDepartment(BoardKind boardKind, Pageable pageable, String schoolName, String department) {
        // 게시판 종류, 학과, 학교 정보를 기반으로 게시글 정보 및 좋아요와 댓글의 개수를 가져옵니다.
        String jpql = "SELECT new TeamProject.TeamProjectWeb.dto.BoardSummaryDTO(b.id, b.title, b.content, b.finalDate, b.author, " +
                "(SELECT COUNT(l) FROM Like l WHERE l.board = b), " +
                "(SELECT COUNT(c) FROM Comment c WHERE c.board = b)) " +
                "FROM Board b WHERE b.boardKind = :boardKind AND b.member.schoolName = :schoolName AND b.member.department = :department order by b.finalDate desc";

        List<BoardSummaryDTO> results = em.createQuery(jpql, BoardSummaryDTO.class)
                .setParameter("boardKind", boardKind)
                .setParameter("schoolName", schoolName)
                .setParameter("department", department)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = em.createQuery("SELECT COUNT(b) FROM Board b WHERE b.boardKind = :boardKind AND b.member.schoolName = :schoolName AND b.member.department = :department", Long.class)
                .setParameter("boardKind", boardKind)
                .setParameter("schoolName", schoolName)
                .setParameter("department", department)
                .getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    public List<Board> findTopByLikeCountAndFinalDate() {
        return em.createQuery("select b from Board b where b.finalDate > :oneWeekAgo and b.boardKind NOT IN :departmentBoards order by b.likeCnt desc, b.chatCnt desc", Board.class)
                .setParameter("oneWeekAgo", LocalDateTime.now().minusDays(7))
                .setParameter("departmentBoards", Arrays.asList(BoardKind.ISSUE, BoardKind.TIP, BoardKind.REPORT, BoardKind.QNA))
                .setMaxResults(4)
                .getResultList();
    }

    public List<Board> findTopByChatCountAndFinalDate() {
        return em.createQuery("select b from Board b where b.finalDate > :oneWeekAgo and b.boardKind NOT IN :departmentBoards order by b.chatCnt desc, b.likeCnt desc", Board.class)
                .setParameter("oneWeekAgo", LocalDateTime.now().minusDays(7))
                .setParameter("departmentBoards", Arrays.asList(BoardKind.ISSUE, BoardKind.TIP, BoardKind.REPORT, BoardKind.QNA))
                .setMaxResults(4)
                .getResultList();
    }
}
