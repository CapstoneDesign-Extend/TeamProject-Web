package TeamProject.TeamProjectWeb.service;


import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class CommentService {
    private final CommentRepository commentRepository;

    public void createComment(Long boardId, Long memberId, String content, String author) {
        // 댓글 작성
        commentRepository.saveComment(boardId, memberId, content, author);
    }

    public void updateComment(Long commentId, String content) {
        // 댓글 수정 -> 해당 댓글 id 가져와 수정
        Comment comment = commentRepository.findById(commentId);
        if (comment != null) {
            comment.setContent(content);
        }
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
}
