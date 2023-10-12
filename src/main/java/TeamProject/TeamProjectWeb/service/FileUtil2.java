package TeamProject.TeamProjectWeb.service;


import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.Images;
import TeamProject.TeamProjectWeb.domain.UploadFile;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true) // 조회 시 readOnly = true 해당 속성을 주면 최적화됨
//@AllArgsConstructor // 현재 클래스가 가지고 있는 필드를 가지고 생성자를 만들어줌
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class FileUtil2 {

    //    private final FileDTO fileDTO;
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    //== 편의 메소드 ==//
    @Value("${file.dir}")
    private String fileDir; // 파일 경로

    public String getFullPath(String fileName) {
        return fileDir + fileName; // 파일경로 + 파일이름 = fileFullName
    }

    public List<UploadFile> uploadFiles(List<MultipartFile> multipartFiles, Long boardId) throws IOException { //== 여러 개의 파일 저장 ==//
        List<UploadFile> fileEntityResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) { // 파일이 존재하면
                fileEntityResult.add(uploadFile(multipartFile, boardId)); // 밑에 작성한 uploadFile 메소드를 이용해 파일 저장
            }
        }
        return fileEntityResult;
    }

    public UploadFile uploadFile(MultipartFile multipartFile, Long boardId) throws IOException { //== 파일 저장 ==//
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename(); // 클라이언트가 업로드한 파일명
        String storeFileName = createStoreFileName(originalFilename); // 서버에 저장할 파일명

        Images images = new Images(originalFilename);
        Board board = boardRepository.findById(boardId);
        images.setBoard(board);
        fileRepository.save(images); // db에도 저장함

        // storeFileName(경로)를 가지고 transferTo() 메소드를 이용해 저장함
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) { //== 서버에 저장할 파일명 만들기 ==//
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext; // uuid.확장자로 리턴함
    }

    private String extractExt(String originalFilename) { //== 확장자만 잘라냄 ==//
        int pos = originalFilename.lastIndexOf(".");

        return originalFilename.substring(pos + 1);
    }
}
