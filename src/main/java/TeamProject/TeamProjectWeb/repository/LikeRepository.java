package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.Like;
import TeamProject.TeamProjectWeb.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Like save(Like like) {
        if (like.getId() == null) {
            em.persist(like);
        } else {
            em.merge(like);
        }
        return like;
    }

    @Transactional
    public Like findOne(Long id) {
        return em.find(Like.class, id);
    }

    @Transactional
    public List<Like> findByMemberAndBoard(Long memberId, Long boardId) {
        return em.createQuery("SELECT l FROM Like l WHERE l.member.id = :memberId AND l.board.id = :boardId", Like.class)
                .setParameter("memberId", memberId)
                .setParameter("boardId", boardId)
                .getResultList();
    }


    @Transactional
    public List<Like> findCommentsByMemberAndBoard(Long memberId, Long boardId) {
        return em.createQuery("SELECT l FROM Like l WHERE l.member.id = :memberId AND l.comment.board.id = :boardId", Like.class)
                .setParameter("memberId", memberId)
                .setParameter("boardId", boardId)
                .getResultList();
    }

    @Transactional
    public List<Like> findByMemberAndComment(Long memberId, Long commentId) {
        return em.createQuery("SELECT l FROM Like l WHERE l.member.id = :memberId AND l.comment.id = :commentId", Like.class)
                .setParameter("memberId", memberId)
                .setParameter("commentId", commentId)
                .getResultList();
    }

    @Transactional
    public List<Like> findAllByBoardId(Long boardId) {
        return em.createQuery("SELECT l FROM Like l WHERE l.board.id = :boardId", Like.class)
                .setParameter("boardId", boardId)
                .getResultList();
    }

    @Transactional
    public List<Like> findAllByCommentId(Long commentId) {
        return em.createQuery("SELECT l FROM Like l WHERE l.comment.id = :commentId", Like.class)
                .setParameter("commentId", commentId)
                .getResultList();
    }

    @Transactional
    public void delete(Like like) {
        em.remove(like);
    }
}
