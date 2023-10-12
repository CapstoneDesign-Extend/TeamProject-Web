package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.FileEntity;
import TeamProject.TeamProjectWeb.domain.Images;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileRepository {
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public FileEntity save(FileEntity fileEntity){
        em.persist(fileEntity);
        return fileEntity;
    }
    //========================= Images 클래스 관련 리파지토리
    @Transactional
    public Images save(Images images){
        em.persist(images);
        return images;
    }
    public Images findImagesByBoardId(Long boardId) {
        return em.createQuery("SELECT i FROM Images i WHERE i.board.id = :boardId", Images.class)
                .setParameter("boardId", boardId)
                .getSingleResult();
    }
    //=========================        END
    public FileEntity findById(Long id){
        return em.find(FileEntity.class, id);
    }

    @Transactional
    public void delete(FileEntity fileEntity){
        em.remove(fileEntity);
    }

    @Transactional
    public void deleteById(Long id) {
        FileEntity fileEntity = em.find(FileEntity.class, id);
        if (fileEntity != null) {
            em.remove(fileEntity);
        }
    }
}
