package TeamProject.TeamProjectWeb.controller.RestApi;


import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.CommentDTO;
import TeamProject.TeamProjectWeb.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentRepository commentRepository;

    // 댓글 작성 API 엔드포인트
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestParam Long boardId,
                                                 @RequestParam Long memberId,
                                                 @RequestParam String content,
                                                 @RequestParam String author) {
        // 댓글 작성을 위해 boardId와 memberId, 댓글 내용(content)을 파라미터로 받음
        // commentRepository의 saveComment 메소드를 호출하여 댓글을 저장함
        // 해당 댓글은 주어진 boardId와 memberId에 해당하는 게시글과 회원에 연결됨
        commentRepository.saveComment(boardId, memberId, content, author);
        // 댓글 작성이 성공적으로 완료되면 200 OK 상태 코드를 반환
        // 추가:: 응답이 비어있으면 안드로이드에서 처리 불가하기 때문에, 빈 객체로 응답하도록 변경
        Comment comment = new Comment();
        return ResponseEntity.ok().body(comment);
    }

    // 댓글 검색 API 엔드포인트 - 특정 게시글의 모든 댓글 조회
    @GetMapping("/{boardId}")
    public List<CommentDTO> getCommentsByBoardId(@PathVariable Long boardId) {
        // 주어진 boardId에 해당하는 게시글의 모든 댓글을 조회하여 반환함
        // commentRepository의 findByBoardId 메소드를 호출하여 댓글 목록을 얻어옴
        return commentRepository.findByBoardId(boardId);
    }

    // 댓글 검색 API 엔드포인트 - 특정 회원의 모든 댓글 조회
    @GetMapping("/member/{memberId}")
    public List<Comment> getCommentsByMemberId(@PathVariable Long memberId) {
        // 주어진 memberId에 해당하는 회원의 모든 댓글을 조회하여 반환함
        // 먼저 memberId를 가지고 있는 Member 객체를 생성하고 commentRepository의 findByMember 메소드를 호출하여 댓글 목록을 얻어옴
        Member member = new Member();
        member.setId(memberId);
        return commentRepository.findByMember(member);
    }

    // 댓글 검색 API 엔드포인트 - 댓글 내용으로 검색
    @GetMapping("/search")
    public List<Comment> searchComments(@RequestParam("content") String content) {
        // 주어진 댓글 내용(content)을 포함하는 모든 댓글을 조회하여 반환함
        // commentRepository의 findByContentContaining 메소드를 호출하여 댓글 목록을 얻어옴
        return commentRepository.findByContentContaining(content);
    }

    // 댓글 삭제 API 엔드포인트 - 댓글 ID로 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long commentId) {
        // 주어진 commentId에 해당하는 댓글을 데이터베이스에서 삭제함
        // commentRepository의 deleteById 메소드를 호출하여 댓글을 삭제함
        commentRepository.deleteById(commentId);
        // 댓글 삭제가 성공적으로 완료되면 204 No Content 상태 코드를 반환
        return ResponseEntity.noContent().build();
    }

    // 특정 회원의 모든 댓글 삭제 API 엔드포인트
    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<Void> deleteCommentsByMemberId(@PathVariable Long memberId) {
        // 주어진 memberId에 해당하는 회원의 모든 댓글을 데이터베이스에서 삭제함
        // 먼저 memberId를 가지고 있는 Member 객체를 생성하고 commentRepository의 deleteByMember 메소드를 호출하여 댓글을 삭제함
        Member member = new Member();
        member.setId(memberId);
        commentRepository.deleteByMember(member);
        // 댓글 삭제가 성공적으로 완료되면 204 No Content 상태 코드를 반환
        return ResponseEntity.noContent().build();
    }
}