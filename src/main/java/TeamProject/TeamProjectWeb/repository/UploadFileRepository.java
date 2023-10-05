package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.UploadFile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UploadFileRepository {
    @PersistenceContext // EntityManager를 주입받기 위해 사용
    private final EntityManager em;

    public UploadFile findById(Long id) { //
        return em.find(UploadFile.class, id);
    }

    public List<UploadFile> findAll() { // 사실상 별로 필요없는 메소드임
        return em.createQuery("SELECT uf FROM UploadFile uf", UploadFile.class)
                .getResultList();
    }

    public void save(UploadFile uploadFile) {
        em.persist(uploadFile);
    }

    public void update(UploadFile uploadFile) {
        em.merge(uploadFile);
    }

    public void delete(UploadFile uploadFile) {
        em.remove(uploadFile);
    }

}
