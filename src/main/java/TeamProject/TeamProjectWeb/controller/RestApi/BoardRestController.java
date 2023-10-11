package TeamProject.TeamProjectWeb.controller.RestApi;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.domain.FileEntity;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.BoardDTO;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import TeamProject.TeamProjectWeb.utils.ConvertDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardRestController {
    private final BoardRepository boardRepository;
    //@Autowired
    private final MemberRepository memberRepository;

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

    @GetMapping("/search/byBoardKindMember")
    public List<BoardDTO> getBoardsByBoardKind(@RequestParam("boardKind") BoardKind boardKind, @RequestParam("memberId") Long memberId) {

        Pair<String, String> userInfo = getUserInfo(memberId);  // <SchoolName, Department>
        String userSchool = userInfo.getLeft();
        String userDepartment = userInfo.getRight();

        if (boardKind == BoardKind.ISSUE
                || boardKind == BoardKind.TIP
                || boardKind == BoardKind.REPORT
                || boardKind == BoardKind.QNA) {

            List<Board> boards = boardRepository.findByBoardKind(boardKind);

            List<BoardDTO> filteredBoards = boards.stream()
                    .filter(board -> {
                        String boardSchool = board.getMember().getSchoolName();
                        String boardDepartment = board.getMember().getDepartment();
                        return userSchool.equals(boardSchool) && userDepartment.equals(boardDepartment);
                    })
                    .map(board -> toDTO(board))
                    .collect(Collectors.toList());

            return filteredBoards;
        } else {
            List<Board> boards = boardRepository.findByBoardKind(boardKind);
            return boards.stream()
                    .map(board -> toDTO(board))
                    .collect(Collectors.toList());
        }
    }

    private Pair<String, String> getUserInfo(Long memberId) {
        // member_id에 해당하는 Member 엔티티를 조회합니다.
        Member member = memberRepository.findOne(memberId);
        if (member != null) {
            // 회원 정보가 있을 경우 학교 이름과 학과를 함께 반환합니다.
            return Pair.of(member.getSchoolName(), member.getDepartment());
        } else {
            // 회원 정보가 없을 경우 null 값들을 반환합니다.
            return Pair.of(null, null);
        }
    }

    private BoardDTO toDTO(Board board) {
        BoardDTO dto = ConvertDTO.convertBoard(board);

        List<String> fileUrls = new ArrayList<>();
        for (FileEntity fileEntity : board.getFileEntities()) {
            String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
            fileUrls.add(url);
        }
        dto.setImageURLs(fileUrls);

        return dto;
    }
//    @GetMapping("/search/byBoardKindMember")
//    public List<BoardDTO> getBoardsByBoardKind(@RequestParam("boardKind") BoardKind boardKind, @RequestParam("memberId") Long memberId) {
//        if (boardKind == BoardKind.ISSUE
//                || boardKind == BoardKind.TIP
//                || boardKind == BoardKind.REPORT
//                || boardKind == BoardKind.QNA)
//        {
//            String userDepartment = getUserDepartment(memberId);
//            if (userDepartment != null){
//                // 먼저 해당 BoardKind에 속한 게시글을 조회하고, 이후 필터링
//                List<Board> boards = boardRepository.findByBoardKind(boardKind);
//                // userDepartment와 동일한 department를 가진 user가 작성한 게시글만 필터링하여 반환
//                List<BoardDTO> filteredBoards = boards.stream()
//                        .filter(board -> {
//                            String boardDepartment = board.getMember() != null ? board.getMember().getDepartment() : null;
//                            // boardDepartment와 userDepartment가 모두 null인 경우는 필터링하지 않음
//                            if (boardDepartment == null && userDepartment == null) {
//                                return true;
//                            }
//                            return userDepartment.equals(boardDepartment);
//                        })
//                        .map(board -> {
//                            BoardDTO dto = ConvertDTO.convertBoard(board);
//
//                            // 해당 게시글에 파일이 있다면 그 URL 리스트도 담아서 반환
//                            List<String> fileUrls = new ArrayList<>();
//                            for (FileEntity fileEntity : board.getFileEntities()) {
//                                String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
//                                fileUrls.add(url);
//                            }
//                            dto.setImageURLs(fileUrls);
//
//                            return dto;
//                        })
//                        .collect(Collectors.toList());
//
//                return filteredBoards;
//            } else {
//                // userDepartment가 null인 경우, 해당 BoardKind에 속한 모든 게시글을 반환
//                List<Board> boards = boardRepository.findByBoardKind(boardKind);
//                return boards.stream()
//                        .map(board -> {
//                            BoardDTO dto = ConvertDTO.convertBoard(board);
//
//                            // 해당 게시글에 파일이 있다면 그 URL 리스트도 담아서 반환
//                            List<String> fileUrls = new ArrayList<>();
//                            for (FileEntity fileEntity : board.getFileEntities()) {
//                                String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
//                                fileUrls.add(url);
//                            }
//                            dto.setImageURLs(fileUrls);
//
//                            return dto;
//                        })
//                        .collect(Collectors.toList());
//            }
//
//        }
//        else {
//            // 주어진 BoardKind를 가진 모든 게시글을 조회함
//            List<Board> boards = boardRepository.findByBoardKind(boardKind);
//            // 조회된 게시글 목록을 반환함
//            return boards.stream()
//                    .map(board -> {
//                        BoardDTO dto = ConvertDTO.convertBoard(board);
//
//                        // 해당 게시글에 파일이 있다면 그 URL 리스트도 담아서 반환
//                        List<String> fileUrls = new ArrayList<>();
//                        for (FileEntity fileEntity : board.getFileEntities()) {
//                            String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
//                            fileUrls.add(url);
//                        }
//                        dto.setImageURLs(fileUrls);
//
//                        return dto;
//                    })
//                    .collect(Collectors.toList());  // DTO를 반환하도록 변환
//        }
//    }
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
//     키워드로 특정 게시판 검색
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


//    // ===========================  최신 로직 : 학과 특화 게시판을 고려한 엔드포인트  ==============================
//    @GetMapping("/search/byKeywordKind")
//    public List<BoardDTO> getBoardsByKeywordKind(
//            @RequestParam("keyword") String keyword,
//            @RequestParam("boardKind") BoardKind boardKind,
//            @RequestParam("memberId") Long memberId) {
//
//        String userDepartment = getUserDepartment(memberId); // memberId로 학과 정보를 가져옴
//
//        List<Board> boards = boardRepository.findByKeywordKind(keyword, boardKind);
//
//        return boards.stream()
//                .filter(board -> {
//                    // 만약 userDepartment가 null 또는 비어 있다면 필터링 없이 모든 게시글 반환
//                    if (userDepartment == null || userDepartment.isEmpty()) {
//                        return true;
//                    }
//                    // board.getMember()가 null이거나 board.getMember().getDepartment()가 null인 경우를 처리
//                    if (board.getMember() == null) {
//                        return false; // board.getMember()가 null인 경우는 필터링하지 않음
//                    } else if(board.getMember().getDepartment() == null){
//                        return false;
//                    }
//
//                    String boardDepartment = board.getMember().getDepartment();
//                    return userDepartment.equals(boardDepartment);
//                })
//                .map(board -> {
//                    BoardDTO dto = ConvertDTO.convertBoard(board);
//
//                    List<String> fileUrls = new ArrayList<>();
//                    for (FileEntity fileEntity : board.getFileEntities()) {
//                        String url = "http://extends.online:5438/api/files/download/" + fileEntity.getId();
//                        fileUrls.add(url);
//                    }
//                    dto.setImageURLs(fileUrls);
//
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }
//
    private String getUserDepartment(Long memberId) {
        // member_id에 해당하는 Member 엔티티를 조회하고, 그 엔티티의 department 값을 가져옵니다.
        Member member = memberRepository.findOne(memberId);
        if (member != null) {
            return member.getDepartment();
        } else {
            return null; // 회원 정보가 없을 경우 null 반환
        }
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
