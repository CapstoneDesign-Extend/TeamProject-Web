package TeamProject.TeamProjectWeb.repository;

import TeamProject.TeamProjectWeb.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}

//    이렇게 간단한 인터페이스를 선언함으로써 UploadFile에 대한 기본 CRUD 작업을 제공받을 수 있습니다.
//    필요에 따라 추가적인 쿼리 메서드를 작성하여 사용할 수도 있습니다.
//
//        JpaRepository를 확장함으로써 다음과 같은 메서드를 사용할 수 있게 됩니다:
//
//        save(): 엔티티 저장
//        findById(): ID로 엔티티 검색
//        findAll(): 모든 엔티티 검색
//        delete(): 엔티티 삭제
//        등등...

//        이 UploadFileRepository 인터페이스를 프로젝트에 추가하면, Spring Data JPA가 알아서 구현체를 생성해줍니다.
//        따라서 별도의 구현 코드 작성 없이 위에서 보셨던 FileUploadController에서 UploadFileRepository를 @Autowired로 주입받아 사용하실 수 있습니다.