package TeamProject.TeamProjectWeb.controller.like;

import TeamProject.TeamProjectWeb.domain.Like;
import TeamProject.TeamProjectWeb.dto.LikeResponse;
import TeamProject.TeamProjectWeb.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// RESTful API를 위한 컨트롤러임을 나타내는 어노테이션입니다.
@RestController
// 생성자를 통한 의존성 주입을 위한 어노테이션입니다.
@RequiredArgsConstructor
// 이 컨트롤러의 기본 URL 경로를 설정하는 어노테이션입니다.
@RequestMapping("/like")
public class LikeController {

    // LikeService 객체를 final로 선언하여 불변하게 만들고, 생성자를 통해 주입받습니다.
    private final LikeService likeService;

    // 게시글에 대한 좋아요 토글 기능을 처리하는 엔드포인트입니다.
    @PostMapping("/board/{boardId}/member/{memberId}")
    public ResponseEntity<LikeResponse> toggleLikeBoard(@PathVariable Long boardId, @PathVariable Long memberId) {
        return ResponseEntity.ok(likeService.toggleLikeBoard(memberId, boardId));
    }

    // 댓글에 대한 좋아요 토글 기능을 처리하는 엔드포인트입니다.
    @PostMapping("/comment/{commentId}/member/{memberId}")
    public ResponseEntity<Like> toggleLikeComment(@PathVariable Long commentId, @PathVariable Long memberId) {
        return ResponseEntity.ok(likeService.toggleLikeComment(memberId, commentId));
    }

    // 게시글에 대한 좋아요 여부를 확인하는 엔드포인트입니다.
    @GetMapping("/board/{boardId}/member/{memberId}/exists")
    public ResponseEntity<Boolean> isBoardLikedByMember(@PathVariable Long boardId, @PathVariable Long memberId) {
        return ResponseEntity.ok(likeService.isBoardLikedByMember(memberId, boardId));
    }

    // 댓글에 대한 좋아요 여부를 확인하는 엔드포인트입니다.
    @GetMapping("/comment/{commentId}/member/{memberId}/exists")
    public ResponseEntity<Boolean> isCommentLikedByMember(@PathVariable Long commentId, @PathVariable Long memberId) {
        return ResponseEntity.ok(likeService.isCommentLikedByMember(memberId, commentId));
    }

    // 나머지 엔드포인트들...
}