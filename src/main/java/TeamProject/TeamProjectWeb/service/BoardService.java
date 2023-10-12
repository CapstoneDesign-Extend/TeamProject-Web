package TeamProject.TeamProjectWeb.service;


import TeamProject.TeamProjectWeb.domain.*;
import TeamProject.TeamProjectWeb.dto.BoardForm;
import TeamProject.TeamProjectWeb.dto.BoardSummaryDTO;
import TeamProject.TeamProjectWeb.dto.FileDTO;
import TeamProject.TeamProjectWeb.dto.MainBoardDTO;
import TeamProject.TeamProjectWeb.repository.BoardRepository;
import TeamProject.TeamProjectWeb.repository.MemberRepository;
import TeamProject.TeamProjectWeb.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static TeamProject.TeamProjectWeb.utils.TimeUtils.timeFriendly;

@Service
@Slf4j
@Transactional
//@AllArgsConstructor // 현재 클래스가 가지고 있는 필드를 가지고 생성자를 만들어줌
@RequiredArgsConstructor // 현재 클래스가 가지고 있는 필드 중 private final 필드만을 가지고 생성자를 만들어줌
public class BoardService {
    private final FileRepository fileRepository;
    private final FileUtil2 fileUtil;
    private final BoardRepository boardRepository; // 게시판 저장소 의존성 주입

    private final MemberRepository memberRepository;

    @Transactional
    public void createBoard(Board board) {
        // 1. 게시글 생성 -> 각각 알맞은 게시판 속성 저장
        boardRepository.save(board);
    }

    @Transactional
    public Board createBoardWithAuthor(BoardForm form, Member loggedInMember, FileDTO fileDTO) throws IOException { // 여기다 이미지 관련 내용 추가
        Board board = form.toBoard();

        // html 에 여러 장 올린 사진을 fileDTO 객체 안에 getter를 이용해 저장 후 가져옴
        List<UploadFile> storeImageFiles = fileUtil.uploadFiles(fileDTO.getImageFiles(), board.getId());

        Images images = new Images();
        images.setBoard(board); // Board 엔티티와의 관계 설정
        images.setItemName(fileDTO.getFileName());
        images.setImageFiles(storeImageFiles);
        fileRepository.save(images); // DB 저장

        log.info("images = {}", images);

        if (form.isAnonymous()) {
            board.setAuthor("익명"); // 익명으로 표시
        } else {
            board.setAuthor(loggedInMember.getLoginId());
        }
        board.setMember(loggedInMember);

        // 게시글 작성 시간을 현재 시간으로 설정한다.
        board.setFinalDate(LocalDateTime.now());  // 게시글 작성 시간 설정
        // 게시글을 생성하는 서비스 메소드를 호출한다.
        boardRepository.save(board);
        return board;
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
    public void deleteBoard(Long boardId, Member loginMember) {
        Board board = boardRepository.findById(boardId);
        if (board == null) {
            throw new IllegalStateException("게시글이 존재하지 않습니다.");
        }
        if (!board.getMember().getId().equals(loginMember.getId())) {
            throw new IllegalStateException("자신의 글만 삭제할 수 있습니다.");
        }
        boardRepository.delete(board);
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

    public List<MainBoardDTO> findRecentBoardsByKindForDepartmentBoard(BoardKind boardKind, int limit, String schoolName, String department) {
        return boardRepository.findRecentBoardsByKindForDepartmentBoard(boardKind, limit, schoolName, department);
    }

    public List<Board> findByBoardKind(BoardKind boardKind) {
        // 특정 게시판 종류로 게시글을 검색하는 메서드
        return boardRepository.findByBoardKind(boardKind);
    }

    public List<Board> findByBoardKindAmount(BoardKind boardKind, int amount) {
        // 특정 게시판 종류로 제한된 수만큼의 게시글을 검색하는 메서드
        return boardRepository.findByBoardKindAmount(boardKind, amount);
    }

    public List<Board> findByKeyword(String keyword) {
        // 키워드로 게시글을 검색하는 메서드
        return boardRepository.findByKeyword(keyword);
    }

    public List<Board> findByKeywordKind(String keyword, BoardKind boardKind) {
        // 키워드와 게시판 종류로 게시글을 검색하는 메서드
        return boardRepository.findByKeywordKind(keyword, boardKind);
    }

    public Page<BoardSummaryDTO> getBoardSummaryByKindAndMember(BoardKind boardKind, Pageable pageable, Member loggedInMember) {
        // 학과 단위 게시판인 경우 학과 정보를 기반으로 게시글 요약 정보 가져오기
        if (boardKind == BoardKind.ISSUE || boardKind == BoardKind.TIP || boardKind == BoardKind.REPORT || boardKind == BoardKind.QNA) {
            String schoolName = loggedInMember.getSchoolName();
            String department = loggedInMember.getDepartment();
            return boardRepository.findSummaryByBoardKindAndSchoolAndDepartment(boardKind, pageable, schoolName, department);
        } else {
            // 학교 단위 게시판인 경우 학교 전체 게시글 요약 정보 가져오기
            return boardRepository.findSummaryByBoardKind(boardKind, pageable);
        }
    }

    public List<Board> findTopByLikeCountAndFinalDate() {
        return boardRepository.findTopByLikeCountAndFinalDate();
    }

    public List<Board> findTopByChatCountAndFinalDate() {
        return boardRepository.findTopByChatCountAndFinalDate();
    }


    public String formatFinalDate(LocalDateTime finalDate) {
        return timeFriendly(finalDate);
    }

    public String formatCommentDate(LocalDateTime commentDate) {
        return timeFriendly(commentDate);
    }

}


//    @Transactional
//    public void deleteBoard(Long boardId) {
//        // 3. 게시글 삭제 -> 해당 member id로 판별
//        // 게시글 ID를 기반으로 게시글을 삭제하는 메서드
//        Board board = boardRepository.findOne(boardId);
//        if (board != null) {
//            boardRepository.delete(board);
//        }
//    }