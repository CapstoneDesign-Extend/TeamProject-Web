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
import jakarta.transaction.Transactional;
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

    @Transactional  // JPA의 변경 감지(Dirty Checking)기능을 사용하면 likeCnt를 수정한 후 boardRepository.save()를 호출할 필요 없음
    @PostMapping("/board/{boardId}/member/{memberId}")
    public LikeDTO addLikeToBoard(@PathVariable Long boardId, @PathVariable Long memberId) {  // 호출할 때마다 like 추가, 삭제를 반복
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findOne(memberId));
        if(!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Member member = memberOptional.get();

        Optional<Like> existingLike = likeRepository.findByMemberAndBoard(member, boardId);
        Board board = boardRepository.findOne(boardId);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            board.setLikeCnt(board.getLikeCnt() - 1);  // likeCnt(Board) --
            return new LikeDTO(); // 좋아요 취소 시
        }

        Like newLike = new Like();
        newLike.setBoard(board);
        newLike.setMember(member);
        newLike.setLikeType(Like.LikeType.POST);
        board.setLikeCnt(board.getLikeCnt() + 1);  // likeCnt(Board) ++
        likeRepository.save(newLike);
        return ConvertDTO.convertLike(newLike);
    }

    @Transactional  // JPA는 변경 감지(Dirty Checking)기능을 제공:: 트랜젝션 내에서 엔터티의 상태를 변경하면 트랜잭션 종료 시점에 해당 변경이 DB에 자동으로 반영됨
    @PostMapping("/comment/{commentId}/member/{memberId}")
    public LikeDTO addLikeToComment(@PathVariable Long commentId, @PathVariable Long memberId) {  // 호출할 때마다 like 추가, 삭제를 반복
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findOne(memberId));
        if(!memberOptional.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Member member = memberOptional.get();

        Optional<Like> existingLike = likeRepository.findByMemberAndComment(member, commentId);
        Comment comment = commentRepository.findById(commentId);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            comment.setLikeCount(comment.getLikeCount() - 1);  // likeCnt(Comment) --
            return new LikeDTO(); // 좋아요 취소 시
        }

        Like newLike = new Like();
        newLike.setComment(comment);
        newLike.setMember(member);
        newLike.setLikeType(Like.LikeType.COMMENT);
        comment.setLikeCount(comment.getLikeCount() + 1); // likeCnt(Comment) ++
        likeRepository.save(newLike);
        return ConvertDTO.convertLike(newLike);
    }
}
