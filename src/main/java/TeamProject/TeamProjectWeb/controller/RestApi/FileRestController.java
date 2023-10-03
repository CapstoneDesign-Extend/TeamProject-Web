package TeamProject.TeamProjectWeb.controller.RestApi;


import TeamProject.TeamProjectWeb.domain.File;
import TeamProject.TeamProjectWeb.repository.FileRepository;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileRestController {
    private final FileRepository fileRepository;

    @Value("${file.dir}")
    private String fileDir; // 파일 경로


    @PostMapping("/upload")
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File storedFile = new File();
        storedFile = storedFile.storeFile(multipartFile);
        File savedFile = fileRepository.save(storedFile);
        return ResponseEntity.ok(savedFile);
    }

    @GetMapping("/download/{serverFileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String serverFileName) {
        File file = fileRepository.findByServerFileName(serverFileName);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(fileDir + serverFileName);
        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            // 여기에서 예외를 처리합니다. 예를 들면, 로그를 기록하거나 에러 메시지를 반환할 수 있습니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading the file.");
        }

        Resource resource = new ByteArrayResource(fileBytes);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    // 파일 저장 API 엔드포인트
    @PostMapping
    public ResponseEntity<File> saveFile(@RequestBody File file) {
        // 파일 저장 메소드를 호출하여 받은 File 객체를 데이터베이스에 저장
        File savedFile = fileRepository.save(file);
        return ResponseEntity.ok(savedFile);
    }

    // 파일 조회 API 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id) {
        // 주어진 ID로 파일 엔티티 조회
        File file = fileRepository.findById(id);
        if (file == null) {
            // 파일이 존재하지 않는 경우 404 Not Found 상태 코드를 반환
            return ResponseEntity.notFound().build();
        }
        // 파일이 존재하는 경우 200 OK 상태 코드와 함께 파일 객체를 반환
        return ResponseEntity.ok(file);
    }

    // 파일 삭제 API 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        // 주어진 ID로 파일 엔티티 조회하여 삭제
        fileRepository.deleteById(id);
        // 파일이 성공적으로 삭제되었을 경우 204 No Content 상태 코드를 반환
        return ResponseEntity.noContent().build();
    }
}