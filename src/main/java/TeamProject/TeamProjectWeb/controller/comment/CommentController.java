package TeamProject.TeamProjectWeb.controller.comment;

import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.BoardForm;
import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.dto.CommentDTO;
import TeamProject.TeamProjectWeb.service.BoardService;
import TeamProject.TeamProjectWeb.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> addComment(@RequestParam Long boardId,
                                        @RequestParam String content,
                                        @RequestParam Boolean isAnonymous,
                                        HttpSession session) {

        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String authorName;

        if (Boolean.TRUE.equals(isAnonymous)) { // 체크박스가 체크되어 있다면 '익명'으로 설정
            authorName = "익명";
        } else {
            authorName = loggedInMember.getLoginId();
        }

        Comment newComment = commentService.addComment(boardId, content, loggedInMember.getId(), authorName);
        return ResponseEntity.ok(newComment);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByBoardId(@RequestParam Long boardId) {
        List<CommentDTO> commentDTOs = commentService.getCommentsByBoardId(boardId);
        return ResponseEntity.ok(commentDTOs);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, HttpSession session) {
        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        commentService.deleteComment(commentId, loggedInMember.getId());
        return ResponseEntity.ok().build();
    }
}

//    @PostMapping("/comment/add")
//    public String addComment(Long boardId, String content, @RequestParam(required = false) boolean anonymous, HttpSession session) {
//        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        if (loggedInMember == null) {
//            return "redirect:/login";
//        }
//
//        String authorName = anonymous ? "익명" : loggedInMember.getLoginId(); // getUsername()은 실제 사용자명을 가져오는 메서드에 따라 변경되어야 합니다.
//        commentService.addComment(boardId, content, loggedInMember.getId(), authorName);
//
//        return "redirect:/reading/" + boardId;
//    }
//
//    @PostMapping("/comment/{commentId}/delete")
//    public String deleteComment(@PathVariable Long commentId, HttpSession session) {
//        Member loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        Comment comment = commentService.findById(commentId);
//        if (comment == null || loggedInMember == null || !comment.getMember().getId().equals(loggedInMember.getId())) {
//            return "redirect:/";
//        }
//        commentService.deleteCommentById(commentId);
//        return "redirect:/reading/" + comment.getBoard().getId();
//    }

//    @PostMapping("/board/{boardId}/comment")
//    public ResponseEntity<?> addComment(@PathVariable Long boardId, @RequestBody CommentForm form, Principal principal) {
//        // 댓글 내용과 현재 로그인된 사용자 정보를 이용해 댓글 추가
//        Comment comment = commentService.addComment(boardId, form.getContent(), principal.getName());
//        if (comment != null) {
//            // 성공적으로 댓글 추가 시 해당 댓글 반환
//            return ResponseEntity.ok(comment);
//        }
//        // 댓글 추가 실패 시 오류 메시지 반환
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add comment");
//    }
//
//    // 댓글 삭제 로직
//    @DeleteMapping("/comment/{commentId}")
//    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, Principal principal) {
//        // 주어진 댓글 ID와 사용자 정보로 댓글 삭제
//        boolean deleted = commentService.deleteCommentById(commentId, principal.getName());
//        if (deleted) {
//            // 성공적으로 댓글 삭제 시 200 OK 응답 반환
//            return ResponseEntity.ok().build();
//        }
//        // 댓글 삭제 실패 시 오류 메시지 반환
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete comment");
//    }