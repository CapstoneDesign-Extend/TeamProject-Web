package TeamProject.TeamProjectWeb.service;


import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.CommentDTO;
import TeamProject.TeamProjectWeb.dto.CommentResponse;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.repository.CommentRepository;
import TeamProject.TeamProjectWeb.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class CommentService {
    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    private final BoardService boardService;

    public Comment addComment2(Long boardId, String content, Long memberId, String authorName) {
        commentRepository.saveComment(boardId, memberId, content, authorName);
        Board board = boardRepository.findOne(boardId);
        // 아래에서 Comment 객체를 반환할 때 새롭게 작성된 댓글의 내용과 해당 게시물의 최종 댓글 수를 함께 반환합니다.
        return new Comment(board.getChatCnt(), content, authorName, LocalDateTime.now());
    }

    public CommentResponse addComment(Long boardId, String content, Long memberId, String authorName) {
        commentRepository.saveComment(boardId, memberId, content, authorName);
        Board board = boardRepository.findOne(boardId);

        CommentResponse response = new CommentResponse();
        response.setChatCnt((long) board.getChatCnt());
        response.setContent(content);
        response.setAuthorName(authorName);
        response.setFormattedFinalDate(boardService.formatCommentDate(LocalDateTime.now()));

        return response;
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }
        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    private void incrementCommentCount(Long boardId) {
        Board board = boardRepository.findOne(boardId);
        board.setChatCnt(board.getChatCnt() + 1);
        boardRepository.save(board);
    }

    private void decrementCommentCount(Long boardId) {
        Board board = boardRepository.findOne(boardId);
        board.setChatCnt(board.getChatCnt() - 1);
        boardRepository.save(board);
    }


    @Transactional(readOnly = true)
    public List<Comment> findCommentsByMember(Member member) {
        // 댓글 검색 -> 회원이 작성한 댓글 모두 조회(아이디)
        return commentRepository.findByMember(member);
    }

    @Transactional(readOnly = true)
    public List<Comment> searchCommentsByContent(String content) {
        // 댓글 검색 -> 비슷한 글자로 찾기
        return commentRepository.findByContentContaining(content);
    }

    public void deleteCommentById(Long commentId) {
        // 댓글 삭제 -> 해당 댓글 id 가져와 삭제
        commentRepository.deleteById(commentId);
    }

    public void deleteCommentsByMember(Member member) {
        // 댓글 삭제 -> 해당 회원이 작성한 댓글 모두 삭제
        commentRepository.deleteByMember(member);
    }

    @Transactional(readOnly = true)
    public Comment findById(Long commentId) {
        // 댓글 ID로 댓글 조회
        return commentRepository.findById(commentId);
    }

    public String formatCommentDate(LocalDateTime commentDate) {
        return TimeUtils.timeFriendly(commentDate); // 예시로 TimeUtils라는 유틸리티 클래스 이름을 사용하였습니다.
    }

}


// 댓글 작성 로직
//    @Transactional
//    public Comment addComment(Long boardId, String content, String username) {
//        // 게시글과 사용자 정보 가져오기
//        Board board = boardRepository.findOne(boardId).orElseThrow(() -> new IllegalArgumentException("No board found with id: " + boardId));
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("No user found with username: " + username));
//
//        // 댓글 객체 생성 후 정보 설정
//        Comment comment = new Comment();
//        comment.setContent(content);
//        comment.setUser(user);
//        comment.setBoard(board);
//        board.getComments().add(comment);  // 게시글의 댓글 목록 업데이트
//        board.setChatCnt(board.getChatCnt() + 1);  // 댓글 수 업데이트
//        return commentRepository.save(comment);  // 댓글 저장 후 반환
//    }



//    // 댓글 삭제 로직
//    @Transactional
//    public boolean deleteCommentById(Long commentId, String username) {
//        // 주어진 ID로 댓글 정보 가져오기
//        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("No comment found with id: " + commentId));
//
//        // 댓글 작성자만 삭제 가능
//        if (!comment.getUser().getUsername().equals(username)) {
//            return false;
//        }
//        commentRepository.delete(comment);  // 댓글 삭제
//        return true;  // 삭제 성공
//    }