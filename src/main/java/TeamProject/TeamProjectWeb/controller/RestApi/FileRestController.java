package TeamProject.TeamProjectWeb.controller.RestApi;


import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.FileEntity;
import TeamProject.TeamProjectWeb.dto.FileDTO;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.repository.FileRepository;
import TeamProject.TeamProjectWeb.service.FileUtil;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileRestController {

    private final BoardRepository boardRepository;
    private final FileUtil fileUtil;
    private final FileRepository fileRepository;


    // ======================= 파일 업로드 ==========================
    @PostMapping("/upload")
    public ResponseEntity<List<FileDTO>> handleFileUpload(@RequestParam("files") List<MultipartFile> files, @RequestParam("boardId") Long boardId) {
        List<FileDTO> savedFileDTOs = new ArrayList<>();
        Board board = boardRepository.findById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("Board not found with id: " + boardId);
        }

        for (MultipartFile file : files) {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            // UUID를 사용하여 고유한 파일 이름 생성
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

            // 로컬에 저장
            File saveFile = new File("C:/Users/JY/Documents/ExtendRAWFiles/" + fileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // DB에 파일 정보 저장
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(fileName);
                fileEntity.setOriginalFileName(originalFileName);
                fileEntity.setContentType(file.getContentType());
                fileEntity.setSize(file.getSize());

                // Board 연관관계 설정
                board.addFileEntity(fileEntity);
                fileEntity = fileRepository.save(fileEntity);

                // DTO를 반환하도록 처리
                savedFileDTOs.add(ConvertDTO.convertFile(fileEntity));

            } catch (IOException e) {
                // 에러 처리
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok(savedFileDTOs);
    }
    // ======================= 특정 게시글에 연결된 이미지 URL 목록 반환 ==========================
    @GetMapping("/urls/{boardId}")
    public ResponseEntity<List<String>> getFileUrlsByBoardId(@PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("Board not found.");
        }

        List<String> fileUrls = new ArrayList<>();

        for (FileEntity fileEntity : board.getFileEntities()) {
            String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
            fileUrls.add(url);
        }

        return ResponseEntity.ok(fileUrls);
    }

    // ======================= 파일 다운로드: 이미지를 직렬화한 후 응답에 담아 반환 ==========================
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        FileEntity fileEntity = fileRepository.findById(id);
        if (fileEntity == null){
            throw new IllegalArgumentException("File not found.");
        }

        Path path = Paths.get("C:/Users/JY/Documents/ExtendRAWFiles/" + fileEntity.getFileName());
        Resource resource;
        try {
            resource = new InputStreamResource(Files.newInputStream(path));
        } catch (IOException e) {
            // 에러 처리
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getOriginalFileName() + "\"")
                .body(resource);
    }
    // ======================= 파일 업데이트 ==========================
    @PostMapping("/update")
    public ResponseEntity<List<FileDTO>> handleFileUpdate(@RequestParam("files") List<MultipartFile> files, @RequestParam("boardId") Long boardId) {
        // 먼저 해당 게시글에 연결된 모든 파일 삭제
        Board board = boardRepository.findById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("Board not found with id: " + boardId);
        }

        for (FileEntity fileEntity : board.getFileEntities()) {
            Path path = Paths.get("C:/Users/JY/Documents/ExtendRAWFiles/" + fileEntity.getFileName());
            try {
                Files.deleteIfExists(path);
                fileRepository.delete(fileEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        board.getFileEntities().clear();

        // 새로운 파일들 업로드
        List<FileDTO> savedFileDTOs = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            // UUID를 사용하여 고유한 파일 이름 생성
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

            // 로컬에 저장
            File saveFile = new File("C:/Users/JY/Documents/ExtendRAWFiles/" + fileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // DB에 파일 정보 저장
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(fileName);
                fileEntity.setOriginalFileName(originalFileName);
                fileEntity.setContentType(file.getContentType());
                fileEntity.setSize(file.getSize());

                // Board 연관관계 설정
                board.addFileEntity(fileEntity);
                fileEntity = fileRepository.save(fileEntity);

                // DTO를 반환하도록 처리
                savedFileDTOs.add(ConvertDTO.convertFile(fileEntity));

            } catch (IOException e) {
                // 에러 처리
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok(savedFileDTOs);
    }

    // ======================= 파일 삭제 ==========================
    @DeleteMapping("/delete/byBoardId/{boardId}")
    @Transactional
    public ResponseEntity<String> deleteFilesByBoardId(@PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId);
        if (board == null) {
            throw new IllegalArgumentException("Board not found.");
        }

        List<FileEntity> fileEntities = new ArrayList<>(board.getFileEntities());

        for (FileEntity fileEntity : fileEntities) {
            // 게시글과 파일 엔터티의 연관 관계 끊기
            board.removeFileEntity(fileEntity);
            fileEntity.setBoard(null);

            // 데이터베이스에서 파일 엔터티 삭제
            fileRepository.delete(fileEntity);

            // 파일 시스템에서 파일 삭제
            Path path = Paths.get("C:/Users/JY/Documents/ExtendRAWFiles/" + fileEntity.getFileName());
            try {
                Files.deleteIfExists(path);  // 파일 시스템에서 파일 삭제
            } catch (IOException e) {
                // 에러 처리
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file: " + fileEntity.getOriginalFileName());
            }
        }

        return ResponseEntity.ok("All files associated with board ID " + boardId + " have been deleted.");
    }
}