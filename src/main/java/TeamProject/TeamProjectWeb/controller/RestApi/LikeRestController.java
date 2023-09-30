package TeamProject.TeamProjectWeb.controller.RestApi;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Like;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.LikeDTO;
import TeamProject.TeamProjectWeb.dto.LikeStatusDTO;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.repository.CommentRepository;
import TeamProject.TeamProjectWeb.repository.LikeRepository;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        List<Like> existingLikes = likeRepository.findByMemberAndBoard(memberId, boardId);
        Board board = boardRepository.findOne(boardId);
        if (!existingLikes.isEmpty()) {
            likeRepository.delete(existingLikes.get(0));
            board.setLikeCnt(board.getLikeCnt() - 1);
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
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        List<Like> existingLikes = likeRepository.findByMemberAndComment(memberId, commentId);
        Comment comment = commentRepository.findById(commentId);
        if (!existingLikes.isEmpty()) {
            likeRepository.delete(existingLikes.get(0));
            comment.setLikeCount(comment.getLikeCount() - 1);
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
    // 클라이언트에서 게시글을 확인하기 직전, 현재 접속자가 liked한 post + comment들에 "좋아요 Clicked" 처리 하기 위한 정보 제공
    @GetMapping("/liked/board-and-comments/{boardId}/member/{memberId}")
    public ResponseEntity<LikeStatusDTO> getLikedBoardAndComments(@PathVariable Long boardId, @PathVariable Long memberId) {

        // Check if the member liked the board
        List<Like> likedBoardList = likeRepository.findByMemberAndBoard(memberId, boardId);
//        for (Like l : likedBoardList){
//            System.out.println(l.get);
//        }
        System.out.println("**************************" + likedBoardList.size());
        boolean likedBoard = !likedBoardList.isEmpty();
        System.out.println("**************************" + likedBoard);


        // Find comments of the board that the member liked
        List<Like> likedComments = likeRepository.findCommentsByMemberAndBoard(memberId, boardId);

        List<Long> commentIds = likedComments.stream()
                .map(like -> like.getComment().getId())
                .collect(Collectors.toList());

        LikeStatusDTO response = new LikeStatusDTO();
        response.setIsLikedBoard(likedBoard);
        response.setLikedCommentIds(commentIds);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/board/{boardId}/member/{memberId}/exists")
    public ResponseEntity<Boolean> isLikedBoard(@PathVariable Long boardId, @PathVariable Long memberId) {
        List<Like> likes = likeRepository.findByMemberAndBoard(memberId, boardId);
        boolean exists = !likes.isEmpty();
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/comment/{commentId}/member/{memberId}/exists")
    public ResponseEntity<Boolean> isLikedComment(@PathVariable Long commentId, @PathVariable Long memberId) {
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        List<Like> likes = likeRepository.findByMemberAndComment(memberId, commentId);
        boolean exists = !likes.isEmpty();

        return ResponseEntity.ok(exists);
    }
}
