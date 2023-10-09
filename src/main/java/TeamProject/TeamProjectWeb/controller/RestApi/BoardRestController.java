package TeamProject.TeamProjectWeb.controller.RestApi;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.domain.FileEntity;
import TeamProject.TeamProjectWeb.dto.BoardDTO;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardRepository boardRepository;

    // 게시글 생성 API 엔드포인트
    @PostMapping
    @Transactional
    public ResponseEntity<BoardDTO> createBoard(@RequestBody Board board) {
        // 게시글을 생성하는 API 엔드포인트로, 요청 바디에서 받은 board 객체를 boardRepository의 save 메소드를 호출하여 데이터베이스에 저장함
        // 저장된 게시글 정보를 ResponseEntity로 포장하여 반환함
        boardRepository.save(board);
        return ResponseEntity.ok(ConvertDTO.convertBoard(board));
    }
    // 특정 id의 게시글을 반환하는 API 엔드포인트 + 이미지 URL 목록도 저장해서 반환
    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id) {
        // 주어진 id에 해당하는 게시글을 조회함
        BoardDTO dto = boardRepository.findOneDTO(id);
        // 해당 게시글에 파일이 있다면 그 URL 리스트도 담아서 반환
        Board board = boardRepository.findById(id);
        if (board == null){
            throw new IllegalArgumentException("Board not found.");
        }
        List<String> fileUrls = new ArrayList<>();
        for (FileEntity fileEntity : board.getFileEntities()) {
            String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
            fileUrls.add(url);
        }
        dto.setImageURLs(fileUrls);

        if (dto == null) {
            // 주어진 id에 해당하는 게시글이 없는 경우 404 Not Found 상태 코드를 반환함
            return ResponseEntity.notFound().build();
        }
        // 주어진 id에 해당하는 게시글 정보를 ResponseEntity로 포장하여 반환함
        return ResponseEntity.ok(dto);
    }
    // 특정 BoardKind 의 게시글 리스트를 반환하는 API 엔드포인트
    @GetMapping("/search/byBoardKind")
    public List<BoardDTO> getBoardsByBoardKind(@RequestParam("boardKind") BoardKind boardKind) {
        // 주어진 BoardKind를 가진 모든 게시글을 조회함
        List<Board> boards = boardRepository.findByBoardKind(boardKind);
        // 조회된 게시글 목록을 반환함
        return boards.stream()
                .map(board -> {
                    BoardDTO dto = ConvertDTO.convertBoard(board);

                    // 해당 게시글에 파일이 있다면 그 URL 리스트도 담아서 반환
                    List<String> fileUrls = new ArrayList<>();
                    for (FileEntity fileEntity : board.getFileEntities()) {
                        String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
                        fileUrls.add(url);
                    }
                    dto.setImageURLs(fileUrls);

                    return dto;
                })
                .collect(Collectors.toList());  // DTO를 반환하도록 변환
    }
    // 특정 BoardKind 의 최신 게시글 리스트를 필요한 만큼만 반환하는 API 엔드포인트
    @GetMapping("/search/byBoardKindAmount")
    public List<BoardDTO> getLatestBoardsByBoardKind(@RequestParam("boardKind") BoardKind boardKind, @RequestParam("amount") int amount) {
        List<Board> boards = boardRepository.findByBoardKindAmount(boardKind, amount);

        return boards.stream()
                .map(board -> {
                    BoardDTO dto = ConvertDTO.convertBoard(board);

                    // 해당 게시글에 파일이 있다면 그 URL 리스트도 담아서 반환
                    List<String> fileUrls = new ArrayList<>();
                    for (FileEntity fileEntity : board.getFileEntities()) {
                        String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
                        fileUrls.add(url);
                    }
                    dto.setImageURLs(fileUrls);

                    return dto;
                })
                .collect(Collectors.toList());  // DTO를 반환하도록 변환
    }

    // 제목으로 검색하는 API 엔드포인트
    @GetMapping("/search/byTitle")
    public List<BoardDTO> getBoardsByTitle(@RequestParam("title") String title) {
        // 주어진 title을 포함하는 모든 게시글을 조회함
        // boardRepository의 findByTitle 메소드를 호출하여 검색된 게시글 목록을 반환함
        return ConvertDTO.convertBoardList(boardRepository.findByTitle(title));
    }
    // 제목과 본문에서 검색하는 API 엔드포인트
    @GetMapping("/search/byKeyword")
    public List<BoardDTO> getBoardsByKeyword(@RequestParam("keyword") String keyword) {
        List<Board> boards = boardRepository.findByKeyword(keyword);

        return boards.stream()
                .map(board -> {
                    BoardDTO dto = ConvertDTO.convertBoard(board);

                    List<String> fileUrls = new ArrayList<>();
                    for (FileEntity fileEntity : board.getFileEntities()) {
                        String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
                        fileUrls.add(url);
                    }
                    dto.setImageURLs(fileUrls);

                    return dto;
                })
                .collect(Collectors.toList());
    }
    // 키워드로 특정 게시판 검색
    @GetMapping("/search/byKeywordKind")
    public List<BoardDTO> getBoardsByKeywordKind(@RequestParam("keyword") String keyword, @RequestParam("boardKind") BoardKind boardKind){
        List<Board> boards = boardRepository.findByKeywordKind(keyword, boardKind);

        return boards.stream()
                .map(board -> {
                    BoardDTO dto = ConvertDTO.convertBoard(board);

                    List<String> fileUrls = new ArrayList<>();
                    for (FileEntity fileEntity : board.getFileEntities()) {
                        String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
                        fileUrls.add(url);
                    }
                    dto.setImageURLs(fileUrls);

                    return dto;
                })
                .collect(Collectors.toList());
    }
    // 모든 게시글 조회 API 엔드포인트
    @GetMapping
    public List<BoardDTO> getAllBoards() {
        // 모든 게시글을 조회함
        // boardRepository의 findAll 메소드를 호출하여 모든 게시글 목록을 반환함
        return ConvertDTO.convertBoardList(boardRepository.findAll());
    }

    // 게시글 수정 API 엔드포인트
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<BoardDTO> updateBoard(@PathVariable Long id, @RequestBody Board updatedBoard) {
        // 주어진 id에 해당하는 게시글을 조회함
        Board board = boardRepository.findOne(id);
        if (board == null) {
            // 주어진 id에 해당하는 게시글이 없는 경우 404 Not Found 상태 코드를 반환함
            return ResponseEntity.notFound().build();
        }
        // 주어진 id에 해당하는 게시글을 수정하기 위해 요청 바디에서 받은 updatedBoard 객체의 정보로 기존 게시글을 업데이트함
        board.setTitle(updatedBoard.getTitle());
        board.setContent(updatedBoard.getContent());
        board.setAuthor((updatedBoard.getAuthor()));
        board.setPrice(updatedBoard.getPrice());
        // 추가적인 필드 업데이트 등 필요한 로직 작성

        // 수정된 게시글을 boardRepository의 save 메소드를 호출하여 데이터베이스에 저장함
        boardRepository.save(board);
        // 수정된 게시글 정보를 ResponseEntity로 포장하여 반환함
        return ResponseEntity.ok(ConvertDTO.convertBoard(board));
    }

    // 게시글 삭제 API 엔드포인트
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        // 주어진 id에 해당하는 게시글을 조회함
        BoardDTO dto = boardRepository.findOneDTO(id);
        if (dto == null) {
            // 주어진 id에 해당하는 게시글이 없는 경우 404 Not Found 상태 코드를 반환함
            return ResponseEntity.notFound().build();
        }
        // 주어진 id에 해당하는 게시글을 boardRepository의 delete 메소드를 호출하여 데이터베이스에서 삭제함
        boardRepository.deleteById(dto.getId());
        // 삭제 성공 시 204 No Content 상태 코드를 반환함
        return ResponseEntity.noContent().build();
    }
}
