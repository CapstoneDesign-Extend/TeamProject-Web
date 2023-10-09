package TeamProject.TeamProjectWeb.repository;



import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.CommentDTO;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    // 댓글 작성 -> 어떤 게시글 댓글인지 확인
    // 댓글 수정 -> 해당 댓글 id 가져와 수정
    // 댓글 검색 -> 회원이 작성한 댓글 모두 조회(아이디), 비슷한 글자로 찾기
    // 댓글 삭제 -> 해당 댓글 id 가져와 삭제 || 해당 회원이 작성한 댓글 모두 삭제
    /*
    Query 클래스 : JPA에서 데이터베이스 쿼리를 실행하고 결과를 반환하기 위해 사용됨
    Query 클래스는 EntityManager를 통해 생성되며,
    DB 쿼리를 실행하기 위한 메소드(createQuery, setParameter, getResultList, executeUpdate 등)를 제공
     */

    @PersistenceContext // EntityManager를 주입받기 위해 사용
    private final EntityManager em;

    @Transactional
    public void saveComment(Long boardId, Long memberId, String content, String author) {
        Member member = em.find(Member.class, memberId); // memberId에 해당하는 Member 객체를 데이터베이스에서 조회
        Board board = em.find(Board.class, boardId); // boardId에 해당하는 Board 객체를 데이터베이스에서 조회

        if (member == null || board == null) {
            // 만약 member나 board가 null인 경우, 잘못된 boardId나 memberId가 전달된 것으로 간주하고 예외를 던짐
            throw new IllegalArgumentException("Invalid boardId or memberId");
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setFinalDate(LocalDateTime.now());
        comment.setLikeCount(0);
        comment.setMember(member);
        comment.setBoard(board);
        comment.setAuthor(author);

        // chatCnt ++
        board.setChatCnt(board.getChatCnt() + 1);

        em.persist(comment);
    }

    @Transactional
    public Comment saveComment2(Long boardId, Long memberId, String content, String author) {
        Member member = em.find(Member.class, memberId); // memberId에 해당하는 Member 객체를 데이터베이스에서 조회
        Board board = em.find(Board.class, boardId); // boardId에 해당하는 Board 객체를 데이터베이스에서 조회

        if (member == null || board == null) {
            // 만약 member나 board가 null인 경우, 잘못된 boardId나 memberId가 전달된 것으로 간주하고 예외를 던짐
            throw new IllegalArgumentException("Invalid boardId or memberId");
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setFinalDate(LocalDateTime.now());
        comment.setLikeCount(0);
        comment.setMember(member);
        comment.setBoard(board);
        comment.setAuthor(author);

        // chatCnt ++
        board.setChatCnt(board.getChatCnt() + 1);

        em.persist(comment);
        return comment;
    }

    public List<CommentDTO> findByBoardId(Long boardId) { // 게시판 id를 가져와 어디 게시판인지 파악함
        // 게시글 ID에 해당하는 댓글 목록 조회
        Query query = em.createQuery("SELECT c FROM Comment c WHERE c.board.id = :boardId");
        query.setParameter("boardId", boardId);
        List<Comment> comments = query.getResultList();

        // Comment 엔터티 목록을 CommentDTO 목록으로 변환
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOs.add(ConvertDTO.convertComment(comment));
        }

        return commentDTOs;
    }


    public List<Comment> findByMember(Member member) { // 멤버로 댓글 목록 조회
        Query query = em.createQuery("SELECT c FROM Comment c WHERE c.member = :member");
        query.setParameter("member", member);
        return query.getResultList();
    }

    public Comment findById(Long commentId) { // 댓글 ID로 댓글 조회
        return em.find(Comment.class, commentId);
    }

    public List<Comment> findByContentContaining(String content) {
        // 내용에 특정 문자열을 포함하는 댓글 목록 조회
        Query query = em.createQuery("SELECT c FROM Comment c WHERE c.content LIKE :content");
        query.setParameter("content", "%" + content + "%");
        return query.getResultList();
    }

    @Transactional
    public void deleteById(Long commentId) {
        // ID로 댓글 조회
        Comment comment = em.find(Comment.class, commentId);
        if (comment != null) {
            // 해당 댓글의 게시글 참조 가져오기
            Board board = comment.getBoard();
            // chatCnt 값을 감소시키는 로직 추가
            board.setChatCnt(board.getChatCnt() - 1);
            em.remove(comment);
        }
    }

    @Transactional
    public void deleteByMember(Member member) {
        // 멤버에 연결된 모든 댓글 조회
        List<Comment> memberComments = em.createQuery("SELECT c FROM Comment c WHERE c.member = :member", Comment.class)
                .setParameter("member", member)
                .getResultList();

        for (Comment comment : memberComments) {
            Board board = comment.getBoard();
            board.setChatCnt(board.getChatCnt() - 1);
        }

        // 멤버로 댓글 삭제
        Query query = em.createQuery("DELETE FROM Comment c WHERE c.member = :member");
        query.setParameter("member", member);
        query.executeUpdate();
    }
}

