package TeamProject.TeamProjectWeb.controller.RestApi;


import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.FileEntity;
import TeamProject.TeamProjectWeb.dto.FileDTO;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.repository.FileRepository;
import TeamProject.TeamProjectWeb.service.FileUtil;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileRestController {
    private final BoardRepository boardRepository;
    private final FileUtil fileUtil;

    @Autowired
    private FileRepository uploadFileRepository;

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
                fileEntity = uploadFileRepository.save(fileEntity);

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
            String url = "/api/file/download/" + fileEntity.getId();
            fileUrls.add(url);
        }

        return ResponseEntity.ok(fileUrls);
    }

    // ======================= 파일 다운로드: 이미지를 직렬화한 후 응답에 담아 반환 ==========================
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        FileEntity fileEntity = uploadFileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("File not found."));

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






    // =======================================================================================




//
//    /*@PostMapping("/upload")
//    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//        File storedFile = new File();
////        storedFile = storedFile.storeFile(multipartFile);
//        File savedFile = fileRepository.save(storedFile);
//        return ResponseEntity.ok(savedFile);
//    }*/
//    @PostMapping("/uploadImage") // 이미지 파일 처리하는 메소드
//    public ResponseEntity<Board> uploadFiles(@RequestParam("imageFiles") List<MultipartFile> multipartFiles, Long boardId) throws IOException { // 매개변수 이미지, 보드Id로 받아서 해당 보드에 저장하게끔
//        List<FileEntity> uploadImageFileEntities = fileUtil.uploadFiles(multipartFiles, boardId); // 받아온 multipartFile을 로컬 경로와 DB에 저장
//
//        Board board = null;
//        if (!multipartFiles.isEmpty()) { // 이미지가 존재하면
//            board = boardRepository.findById(boardId);
//            board.setImageFileEntities(uploadImageFileEntities); // 이미지들을 저장
//            boardRepository.update(board); // 병합 시킨다.
//        }
//        return ResponseEntity.ok(board); // 파일을 저장한 객체 반환
//    }
//    @PostMapping("/uploadAttach") // 첨부파일 처리하는 메소드
//    public ResponseEntity<Board> uploadFile(@RequestParam("attachFile") MultipartFile multipartFile, Long boardId) throws IOException { // 매개변수 파일, 보드Id로 받아서 해당 보드에 저장하게끔
//        FileEntity attachFileEntity = fileUtil.uploadFile(multipartFile, boardId);
//
//        Board board = null;
//        if (!multipartFile.isEmpty()) { // 첨부파일이 존재하면
//            board = boardRepository.findById(boardId);
//            board.setAttachFileEntity(attachFileEntity); // 첨부파일을 저장
//            boardRepository.update(board); // 병합 시킨다.
//        }
//        return ResponseEntity.ok(board);
//    }
//    @ResponseBody
//    @GetMapping("images/{filename}") // 이미지 저장 url
//    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
//        return new UrlResource("file:"+fileUtil.getFullPath(filename));
//    }


//    @GetMapping("/download/{fileId}") // 파일 저장 url
//    public ResponseEntity<Resource> downloadFile(@PathVariable Long boardId) throws MalformedURLException {
//        Board board = boardRepository.findById(boardId); // 넘어온 id값으로 게시판 가져옴
//        if(board.getAttachFileEntity() == null){ // 저장된 파일이 없으면
//            return ResponseEntity.notFound().build();
//        }
//
//        String serverFileName = board.getAttachFileEntity().getFileName(); // 해당 게시판에 저장된 파일 서버 이름 가져옴
//        String uploadFileName = board.getAttachFileEntity().getOriginalFileName(); // 해당 게시판에 저장된 파일 이름 가져옴
//        UrlResource resource = new UrlResource("file:"+fileUtil.getFullPath(serverFileName)); // 지정한 경로에 파일 저장
//
//        // 저장될 때 클라이언트가 저장한 파일명으로 저장
//        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
//        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(resource);
//    }


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
//    @PostMapping
//    public ResponseEntity<Board> saveBoard(@RequestBody Board board) {
//        // 파일 저장 메소드를 호출하여 받은 Board 객체를 데이터베이스에 저장
//        Board savedBoard = boardRepository.save(board);
//        return ResponseEntity.ok(savedBoard);
//    }
//
//    // 파일 조회 API 엔드포인트
//    @GetMapping("/{id}")
//    public ResponseEntity<Board> getFileById(@PathVariable Long id) {
//        // 주어진 ID로 파일 엔티티 조회
//        Board board = boardRepository.findById(id);
//        if (board.getAttachFileEntity() == null) {
//            // 파일이 존재하지 않는 경우 404 Not Found 상태 코드를 반환
//            return ResponseEntity.notFound().build();
//        }
//        // 파일이 존재하는 경우 200 OK 상태 코드와 함께 파일을 가지고 있는 게시판 객체를 반환
//        return ResponseEntity.ok(board);
//    }
//
//    // 파일 삭제 API 엔드포인트
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
//        // 주어진 ID로 파일 엔티티 조회하여 삭제
//        boardRepository.deleteById(id);
//        // 파일이 성공적으로 삭제되었을 경우 204 No Content 상태 코드를 반환
//        return ResponseEntity.noContent().build();
//    }
}