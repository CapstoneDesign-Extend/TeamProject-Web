package TeamProject.TeamProjectWeb.service;

import TeamProject.TeamProjectWeb.domain.File;
import TeamProject.TeamProjectWeb.repository.FileRepository;
import TeamProject.TeamProjectWeb.repository.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true) // 조회 시 readOnly = true 해당 속성을 주면 최적화됨
//@AllArgsConstructor // 현재 클래스가 가지고 있는 필드를 가지고 생성자를 만들어줌
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class FileService {

    //    private final FileDTO fileDTO;
    private final FileRepository fileRepository;

    public void saveFile1(java.io.File file) { // 파일 받아옴
        String fileName = file.getName(); // 파일명
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1); // 파일 확장자 ex) txt, pdf 등등

        File fileDTO = new File();
        // 파일 엔티티 생성 및 속성 설정
        fileDTO.setFileName(fileName); // 파일 이름 저장
        fileDTO.setFileType(fileType); // 파일 확장자 저장
        // ... 파일 데이터 등 다른 속성 설정

        // 파일 엔티티를 DB에 저장하는 로직 수행
        fileRepository.save(fileDTO);
    }
    public void saveFile2(String filePath) { // 파일 경로로 파일을 찾아옴
        try {
            // 파일 데이터를 읽어옵니다.
            byte[] fileData = FileUtil.readFileData(filePath);

            // FileEntity 객체를 생성하고 데이터를 설정합니다.
            File fileDTO = new File();
            fileDTO.setFileName(FileUtil.getFileName(filePath)); // 파일 이름 추출
            fileDTO.setFileType(FileUtil.getFileExtension(filePath)); // 파일 경로에서 파일 확장자를 추출
            fileDTO.setFileData(fileData);

            // FileEntity를 데이터베이스에 저장합니다.
            fileRepository.save(fileDTO);

        } catch (IOException e) {
            // 파일 읽기 오류 처리
            e.printStackTrace();
        }
    }

}

