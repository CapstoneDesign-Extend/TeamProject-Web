package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.FileEntity;
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
