package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.File;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository // 자동으로 스프링 bean으로 사용됨
@RequiredArgsConstructor
public class FileRepository { // CRUD를 수행하는 클래스

    @PersistenceContext
    private final EntityManager em;

    //-- 파일 저장 --//
    public File save(File file) {
        // 파일 엔티티를 영속화하여 데이터베이스에 저장
        em.persist(file);
        return file;
    }

    //-- 파일 찾기 --//
    public File findById(Long id) {
        // 주어진 ID로 파일 엔티티 조회
        return em.find(File.class, id);
    }
    public File findByServerFileName(String serverFileName) {
        String jpql = "SELECT f FROM File f WHERE f.serverFileName = :serverFileName";
        return em.createQuery(jpql, File.class)
                .setParameter("serverFileName", serverFileName)
                .getSingleResult();
    }
    public File updateFileById(Long id, File updatedFile) {
        File existingFile = em.find(File.class, id); // ID로 엔티티 조회

        if (existingFile != null) {
            // 엔티티를 찾았을 경우, 업데이트할 내용을 설정
            existingFile.setFileName(updatedFile.getFileName());
            existingFile.setUploadFileName(updatedFile.getUploadFileName());
            existingFile.setServerFileName(updatedFile.getServerFileName());

            // 엔티티를 merge하여 수정
            return em.merge(existingFile);
        } else {
            // 엔티티를 찾지 못했을 경우 예외 처리 또는 필요한 로직 수행
            throw new EntityNotFoundException("File with ID " + id + " not found.");
        }
    }

    //-- 파일 삭제 --//
    public void deleteById(Long id) {
        // 주어진 ID로 파일 엔티티 조회하여 삭제
        File file = findById(id);
        if (file != null) {
            em.remove(file);
        }
    }

    /*
    //-- 파일 전체 조회 후 리스트 --//
    public List<FileDTO> findAll() {
        // 모든 파일 엔티티를 조회하여 리스트로 반환
        return em.createQuery("SELECT f FROM FileDTO f", FileDTO.class)
                .getResultList();
    }
    */

    // 기타 필요한 메소드들 작성

}