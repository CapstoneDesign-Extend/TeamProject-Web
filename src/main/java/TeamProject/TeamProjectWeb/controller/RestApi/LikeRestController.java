package TeamProject.TeamProjectWeb.controller.RestApi;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Like;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.LikeDTO;
import TeamProject.TeamProjectWeb.repository.LikeRepository;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeRestController {
    private final LikeRepository likeRepository;

    @PostMapping("/board/{boardId}")
    public LikeDTO addLikeToBoard(@PathVariable Long boardId, @RequestBody Member member) {
        Optional<Like> existingLike = likeRepository.findByMemberAndBoard(member, boardId);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return null; // 좋아요 취소 시
        }
        Like newLike = new Like();
        Board board = new Board(); // Assuming you have a default constructor
        board.setId(boardId);  // id만 넣어도 지연 로딩(fetch = FetchType.LAZY)을 통해 별도 쿼리 없이 JPA가 다른 필드에도 접근할 수 있다고 함
        newLike.setBoard(board);
        newLike.setMember(member);
        newLike.setLikeType(Like.LikeType.POST);
        likeRepository.save(newLike);
        return ConvertDTO.convertLike(newLike);
    }

    @PostMapping("/comment/{commentId}")
    public LikeDTO addLikeToComment(@PathVariable Long commentId, @RequestBody Member member) {
        Optional<Like> existingLike = likeRepository.findByMemberAndComment(member, commentId);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return null; // 좋아요 취소 시
        }
        Like newLike = new Like();
        Comment comment = new Comment(); // Assuming you have a default constructor
        comment.setId(commentId);  // id만 넣어도 지연 로딩(fetch = FetchType.LAZY)을 통해 별도 쿼리 없이 JPA가 다른 필드에도 접근할 수 있다고 함
        newLike.setComment(comment);
        newLike.setMember(member);
        newLike.setLikeType(Like.LikeType.COMMENT);
        likeRepository.save(newLike);
        return ConvertDTO.convertLike(newLike);
    }
}
