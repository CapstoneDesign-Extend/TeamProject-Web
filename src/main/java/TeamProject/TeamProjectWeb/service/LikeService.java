package TeamProject.TeamProjectWeb.service;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Like;
import TeamProject.TeamProjectWeb.dto.LikeResponse;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.repository.CommentRepository;
import TeamProject.TeamProjectWeb.repository.LikeRepository;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 스프링의 서비스 레이어를 나타내는 어노테이션. 스프링 컨테이너에 이 클래스를 서비스로 등록합니다.
@Service
// 생성자 주입을 위한 Lombok 어노테이션. final 로 선언된 모든 필드에 대한 생성자를 자동으로 만들어줍니다.
@RequiredArgsConstructor
// 클래스 레벨의 트랜잭션 설정. 기본적으로 읽기 전용 트랜잭션입니다.
// 하지만 아래의 메서드들에서 명시적으로 트랜잭션 설정을 오버라이드할 수 있습니다.

public class LikeService {

    // 좋아요 관련 데이터 접근 객체
    private final LikeRepository likeRepository;

    // 멤버 관련 데이터 접근 객체
    private final MemberRepository memberRepository;

    // 게시글 관련 데이터 접근 객체
    private final BoardRepository boardRepository;

    // 댓글 관련 데이터 접근 객체
    private final CommentRepository commentRepository;

    // 게시글에 대한 좋아요의 토글 기능을 수행하는 메서드.
    // 존재하는 좋아요는 삭제하고, 없다면 새로 만듭니다.
    @Transactional
    public LikeResponse toggleLikeBoard(Long memberId, Long boardId) {
        List<Like> existingLikes = likeRepository.findByMemberAndBoard(memberId, boardId);
        Board board = boardRepository.findOne(boardId);

        boolean isLiked = false;
        if (!existingLikes.isEmpty()) {
            likeRepository.delete(existingLikes.get(0));
            board.decrementLikeCount(); // 게시물 좋아요 수 감소
        } else {
            Like newLike = new Like();
            newLike.setMember(memberRepository.findOne(memberId));
            newLike.setBoard(board);
            newLike.setLikeType(Like.LikeType.POST);
            likeRepository.save(newLike);
            board.incrementLikeCount(); // 게시물 좋아요 수 증가
            isLiked = true;
        }

        return new LikeResponse(isLiked, board.getLikeCnt());
    }

    // 댓글에 대한 좋아요의 토글 기능을 수행하는 메서드.
    @Transactional
    public Like toggleLikeComment(Long memberId, Long commentId) {
        // 주어진 멤버 ID와 댓글 ID를 사용해 기존의 좋아요를 찾습니다.
        List<Like> existingLikes = likeRepository.findByMemberAndComment(memberId, commentId);

        // 좋아요가 이미 존재한다면 삭제
        if (!existingLikes.isEmpty()) {
            likeRepository.delete(existingLikes.get(0));
            return null;
        }

        // 새로운 좋아요 객체 생성 및 설정
        Like newLike = new Like();
        newLike.setMember(memberRepository.findOne(memberId));   // 멤버 설정
        newLike.setComment(commentRepository.findById(commentId)); // 댓글 설정
        newLike.setLikeType(Like.LikeType.COMMENT); // 좋아요 유형 설정: 댓글

        return likeRepository.save(newLike); // 저장 후 결과 반환
    }

    // 주어진 멤버가 특정 게시글에 좋아요를 눌렀는지 확인하는 메서드
    public boolean isBoardLikedByMember(Long memberId, Long boardId) {
        return !likeRepository.findByMemberAndBoard(memberId, boardId).isEmpty();
    }

    // 주어진 멤버가 특정 댓글에 좋아요를 눌렀는지 확인하는 메서드
    public boolean isCommentLikedByMember(Long memberId, Long commentId) {
        return !likeRepository.findByMemberAndComment(memberId, commentId).isEmpty();
    }

    // 나머지 기능들...
}


//    @Transactional
//    public Like toggleLikeBoard(Long memberId, Long boardId) {
//        // 주어진 멤버 ID와 게시글 ID를 사용해 기존의 좋아요를 찾습니다.
//        List<Like> existingLikes = likeRepository.findByMemberAndBoard(memberId, boardId);
//
//        // 좋아요가 이미 존재한다면 삭제
//        if (!existingLikes.isEmpty()) {
//            likeRepository.delete(existingLikes.get(0));
//            return null;
//        }
//
//        // 새로운 좋아요 객체 생성 및 설정
//        Like newLike = new Like();
//        newLike.setMember(memberRepository.findOne(memberId)); // 멤버 설정
//        newLike.setBoard(boardRepository.findOne(boardId));   // 게시글 설정
//        newLike.setLikeType(Like.LikeType.POST); // 좋아요 유형 설정: 게시글
//
//        return likeRepository.save(newLike); // 저장 후 결과 반환
//    }