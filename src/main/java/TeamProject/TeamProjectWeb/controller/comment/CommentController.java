package TeamProject.TeamProjectWeb.controller.comment;

import TeamProject.TeamProjectWeb.dto.BoardForm;
import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.service.BoardService;
import TeamProject.TeamProjectWeb.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;

    // 댓글 작성 폼 페이지를 보여줍니다.
    @GetMapping("/write")
    public String writeForm(@RequestParam Long boardId, @ModelAttribute("commentForm") BoardForm form) {
        Board board = boardService.findBoardById(boardId);
        if (board == null) {
            // 게시글이 존재하지 않으면 메인 페이지로 리다이렉트
            return "redirect:/";
        }
        return "comment/writeForm";
    }

    // 댓글 작성을 처리합니다.
    @PostMapping("/write")
    public String write(@RequestParam Long boardId,
                        @RequestParam Long memberId,
                        @Valid @ModelAttribute BoardForm form,
                        @RequestParam String author,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "comment/writeForm";
        }
        // 댓글 작성
        commentService.createComment(boardId, memberId, form.getContent(), author);
        return "redirect:/board/" + boardId;
    }

    // 댓글 수정 폼 페이지를 보여줍니다.
    @GetMapping("/{commentId}/edit")
    public String editForm(@PathVariable Long commentId, Model model) {
        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            // 댓글이 존재하지 않으면 메인 페이지로 리다이렉트
            return "redirect:/";
        }
        // 기존 댓글 정보를 가져와서 댓글 수정 폼 페이지에 보여줌
        model.addAttribute("commentForm", comment);
        return "comment/editForm";
    }

    // 댓글 수정을 처리합니다.
    @PostMapping("/{commentId}/edit")
    public String edit(@PathVariable Long commentId,
                       @Valid @ModelAttribute BoardForm form,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "comment/editForm";
        }
        // 댓글 수정
        commentService.updateComment(commentId, form.getContent());
        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            // 댓글이 존재하지 않으면 메인 페이지로 리다이렉트
            return "redirect:/";
        }
        return "redirect:/board/" + comment.getBoard().getId();
    }

    // 댓글 삭제를 처리합니다.
    @PostMapping("/{commentId}/delete")
    public String delete(@PathVariable Long commentId) {
        Comment comment = commentService.findById(commentId);
        if (comment != null) {
            // 댓글 삭제
            commentService.deleteCommentById(commentId);
            return "redirect:/board/" + comment.getBoard().getId();
        } else {
            // 댓글이 존재하지 않으면 메인 페이지로 리다이렉트
            return "redirect:/";
        }
    }
}