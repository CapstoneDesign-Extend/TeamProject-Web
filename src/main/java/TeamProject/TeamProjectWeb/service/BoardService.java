package TeamProject.TeamProjectWeb.service;


import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.dto.MainBoardDTO;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true) // 조회 시 readOnly = true 해당 속성을 주면 최적화됨
//@AllArgsConstructor // 현재 클래스가 가지고 있는 필드를 가지고 생성자를 만들어줌
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class BoardService {
    @Value("${file.dir}")
    private String fileDir; // 파일 경로
    private final BoardRepository boardRepository; // 게시판 저장소 의존성 주입

    @Transactional
    public void createBoard(Board board) {
        // 1. 게시글 생성 -> 각각 알맞은 게시판 속성 저장
        boardRepository.save(board);
    }

    @Transactional
    public void createImageBoard(Board board, MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID(); // 동일한 파일 이름으로 저장되는 것을 막음
        // file.getOriginalFilename() : 클라이언트가 저장한 파일 이름
        String fileName = uuid + "_" + file.getOriginalFilename(); // 서버에 저장할 파일 이름 만들기

        File saveFile = new File(fileDir, fileName); // 해당 경로에 해당 이름으로 저자욀 파일 생성
        file.transferTo(saveFile); // 저장함

        boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(Long boardId, Board board) {
        // 2. 게시글 수정 -> 해당 권한을 가진 member만 수정 가능
        // 게시글 ID를 기반으로 게시글 내용을 수정하는 메서드
        Board updatedBoard = boardRepository.findOne(boardId);
        if (updatedBoard != null) {
            // 게시글 수정 로직 구현
            updatedBoard.setTitle(board.getTitle());
            updatedBoard.setContent(board.getContent());
            updatedBoard.setFinalDate(board.getFinalDate());
        }
        return updatedBoard; // 수정된 게시글 반환
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        // 3. 게시글 삭제 -> 해당 member id로 판별
        // 게시글 ID를 기반으로 게시글을 삭제하는 메서드
        Board board = boardRepository.findOne(boardId);
        if (board != null) {
            boardRepository.delete(board);
        }
    }

    public Board findBoardById(Long boardId) {
        // 게시글 ID로 게시글 조회
        // 게시글 ID를 기반으로 게시글을 검색하는 메서드
        return boardRepository.findOne(boardId);
    }

    public List<Board> findAllBoards() {
        // 모든 게시글 조회
        // 모든 게시글을 반환하는 메서드
        return boardRepository.findAll();
    }

    public List<Board> searchBoardsByTitle(String title) {
        // 4. 게시글 검색 -> 연관된 제목으로 검색
        // 제목을 기반으로 게시글을 검색하는 메서드
        return boardRepository.findByTitle(title);
    }

    public Page<Board> findBoardListWithPaging(Pageable pageable) {
        // 페이지 정보를 기반으로 게시글 목록을 반환하는 메서드
        List<Board> boards = boardRepository.findBoardListWithPaging(pageable);
        long totalCount = boardRepository.count(); // 전체 게시글 수 카운트
        return new PageImpl<>(boards, pageable, totalCount); // 페이지 정보와 함께 게시글 목록 반환
    }

    public Page<Board> findBoardListByKindWithPaging(BoardKind boardKind, Pageable pageable) {
        // 게시판 종류와 페이지 정보를 기반으로 게시글 목록을 반환하는 메서드
        List<Board> boards = boardRepository.findBoardListByKindWithPaging(boardKind, pageable);
        long totalCount = boardRepository.countByBoardKind(boardKind); // 특정 게시판의 전체 게시글 수 카운트
        return new PageImpl<>(boards, pageable, totalCount); // 페이지 정보와 함께 게시글 목록 반환
    }

    public List<MainBoardDTO> findRecentBoardsForMainPage(int limit) {
        // 최근 게시글을 제한 수만큼 반환하는 메서드
        return boardRepository.findRecentBoardsForMainPage(limit);
    }

    public List<MainBoardDTO> findRecentBoardsByKindForMainPage(BoardKind boardKind, int limit) {
        // 특정 게시판의 최근 게시글을 제한 수만큼 반환하는 메서드
        return boardRepository.findRecentBoardsByKindForMainPage(boardKind, limit);
    }

}
