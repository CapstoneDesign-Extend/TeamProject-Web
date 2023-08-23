package TeamProject.TeamProjectWeb.controller.RestApi;


import TeamProject.TeamProjectWeb.domain.File;
import TeamProject.TeamProjectWeb.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileRestController {
    private final FileRepository fileRepository;

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