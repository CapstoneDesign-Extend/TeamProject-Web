package TeamProject.TeamProjectWeb.controller.RestApi;


import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.UploadFile;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.service.FileUtil;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileRestController {
    private final BoardRepository boardRepository;
    private final FileUtil fileUtil;

    /*@PostMapping("/upload")
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File storedFile = new File();
//        storedFile = storedFile.storeFile(multipartFile);
        File savedFile = fileRepository.save(storedFile);
        return ResponseEntity.ok(savedFile);
    }*/
    @PostMapping("/uploadImage") // 이미지 파일 처리하는 메소드
    public ResponseEntity<Board> uploadFiles(@RequestParam("imageFiles") List<MultipartFile> multipartFiles, Long boardId) throws IOException { // 매개변수 이미지, 보드Id로 받아서 해당 보드에 저장하게끔
        List<UploadFile> uploadImageFiles = fileUtil.uploadFiles(multipartFiles, boardId); // 받아온 multipartFile을 로컬 경로와 DB에 저장

        Board board = null;
        if (!multipartFiles.isEmpty()) { // 이미지가 존재하면
            board = boardRepository.findById(boardId);
            board.setImageFiles(uploadImageFiles); // 이미지들을 저장
            boardRepository.update(board); // 병합 시킨다.
        }
        return ResponseEntity.ok(board); // 파일을 저장한 객체 반환
    }
    @PostMapping("/uploadAttach") // 첨부파일 처리하는 메소드
    public ResponseEntity<Board> uploadFile(@RequestParam("attachFile") MultipartFile multipartFile, Long boardId) throws IOException { // 매개변수 파일, 보드Id로 받아서 해당 보드에 저장하게끔
        UploadFile attachFile = fileUtil.uploadFile(multipartFile, boardId);

        Board board = null;
        if (!multipartFile.isEmpty()) { // 첨부파일이 존재하면
            board = boardRepository.findById(boardId);
            board.setAttachFile(attachFile); // 첨부파일을 저장
            boardRepository.update(board); // 병합 시킨다.
        }
        return ResponseEntity.ok(board);
    }
    @ResponseBody
    @GetMapping("images/{filename}") // 이미지 저장 url
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:"+fileUtil.getFullPath(filename));
    }
    @GetMapping("/download/{fileId}") // 파일 저장 url
    public ResponseEntity<Resource> downloadFile(@PathVariable Long boardId) throws MalformedURLException {
        Board board = boardRepository.findById(boardId); // 넘어온 id값으로 게시판 가져옴
        if(board.getAttachFile() == null){ // 저장된 파일이 없으면
            return ResponseEntity.notFound().build();
        }
        
        String serverFileName = board.getAttachFile().getServerFileName(); // 해당 게시판에 저장된 파일 서버 이름 가져옴
        String uploadFileName = board.getAttachFile().getUploadFileName(); // 해당 게시판에 저장된 파일 이름 가져옴
        UrlResource resource = new UrlResource("file:"+fileUtil.getFullPath(serverFileName)); // 지정한 경로에 파일 저장

        // 저장될 때 클라이언트가 저장한 파일명으로 저장
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
    /*@GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable String serverFileName) {
        UploadFile file = fileRepository.findByServerFileName(serverFileName);
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
    }*/
//--------------------------------------------------------------------------------------------------------//
    // 파일 저장 API 엔드포인트
    @PostMapping
    public ResponseEntity<Board> saveBoard(@RequestBody Board board) {
        // 파일 저장 메소드를 호출하여 받은 Board 객체를 데이터베이스에 저장
        Board savedBoard = boardRepository.save(board);
        return ResponseEntity.ok(savedBoard);
    }

    // 파일 조회 API 엔드포인트
    @GetMapping("/{id}")
    public ResponseEntity<Board> getFileById(@PathVariable Long id) {
        // 주어진 ID로 파일 엔티티 조회
        Board board = boardRepository.findById(id);
        if (board.getAttachFile() == null) {
            // 파일이 존재하지 않는 경우 404 Not Found 상태 코드를 반환
            return ResponseEntity.notFound().build();
        }
        // 파일이 존재하는 경우 200 OK 상태 코드와 함께 파일을 가지고 있는 게시판 객체를 반환
        return ResponseEntity.ok(board);
    }

    // 파일 삭제 API 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        // 주어진 ID로 파일 엔티티 조회하여 삭제
        boardRepository.deleteById(id);
        // 파일이 성공적으로 삭제되었을 경우 204 No Content 상태 코드를 반환
        return ResponseEntity.noContent().build();
    }
}