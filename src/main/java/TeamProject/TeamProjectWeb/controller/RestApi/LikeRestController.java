package TeamProject.TeamProjectWeb.controller.RestApi;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Like;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.LikeDTO;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.repository.CommentRepository;
import TeamProject.TeamProjectWeb.repository.LikeRepository;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeRestController {
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @PostMapping("/board/{boardId}/member/{memberId}")
    public LikeDTO addLikeToBoard(@PathVariable Long boardId, @PathVariable Long memberId) {  // 호출할 때마다 like 추가, 삭제를 반복
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findOne(memberId));
        if(!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Member member = memberOptional.get();

        Optional<Like> existingLike = likeRepository.findByMemberAndBoard(member, boardId);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return new LikeDTO(); // 좋아요 취소 시
        }

        Like newLike = new Like();
        Board board = boardRepository.findOne(boardId); // JPA provides this for reference
        newLike.setBoard(board);
        newLike.setMember(member);
        newLike.setLikeType(Like.LikeType.POST);
        likeRepository.save(newLike);
        return ConvertDTO.convertLike(newLike);
    }

    @PostMapping("/comment/{commentId}/member/{memberId}")
    public LikeDTO addLikeToComment(@PathVariable Long commentId, @PathVariable Long memberId) {  // 호출할 때마다 like 추가, 삭제를 반복
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findOne(memberId));
        if(!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Member member = memberOptional.get();

        Optional<Like> existingLike = likeRepository.findByMemberAndComment(member, commentId);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return new LikeDTO(); // 좋아요 취소 시
        }

        Like newLike = new Like();
        Comment comment = commentRepository.findById(commentId); // JPA provides this for reference
        newLike.setComment(comment);
        newLike.setMember(member);
        newLike.setLikeType(Like.LikeType.COMMENT);
        likeRepository.save(newLike);
        return ConvertDTO.convertLike(newLike);
    }
}
