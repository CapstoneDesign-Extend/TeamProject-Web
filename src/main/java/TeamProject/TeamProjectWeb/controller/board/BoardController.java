package TeamProject.TeamProjectWeb.controller.board;

import TeamProject.TeamProjectWeb.constants.BoardConstants;
import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.domain.Comment;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.BoardForm;
import TeamProject.TeamProjectWeb.dto.BoardSummaryDTO;
import TeamProject.TeamProjectWeb.dto.MainBoardDTO;
import TeamProject.TeamProjectWeb.service.BoardService;
import TeamProject.TeamProjectWeb.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final LikeService likeService;

    // 게시글 작성 폼 페이지를 보여줍니다.
    @GetMapping("/writing/write")
    public String writeForm(Model model) {
        BoardForm form = new BoardForm(); // 초기화
        model.addAttribute("board", form); // 뷰로 전달
        model.addAttribute("loggedIn", true);
        return "board/writing/write";
    }

    // 게시글 작성을 처리합니다.
    @PostMapping("/writing/write")
    public String write(@Valid @ModelAttribute BoardForm form,  // 게시글 작성 폼 데이터 바인딩
                        BindingResult bindingResult, // 폼 검증 결과
                        HttpServletRequest request) {  // 현재의 요청 객체
        // 만약 폼 검증에서 오류가 발생했다면
        if (bindingResult.hasErrors()) {
            // 다시 게시글 작성 페이지로 돌아간다.
            return "board/writing/write";
        }

        // 현재의 세션을 가져온다. 없다면 null 을 반환한다.
        HttpSession session = request.getSession(false);
        Member loggedInMember = null;
        if (session != null) {
            loggedInMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        }

        boardService.createBoardWithAuthor(form, loggedInMember);
        // 게시글 작성이 완료되면 홈페이지로 리다이렉트한다.
        return "redirect:/board/board_collection";
    }

    // 게시글 상세 페이지를 보여줍니다.
    @GetMapping("/reading/{boardId}")
    public String view(@PathVariable Long boardId, Model model, HttpSession session) {
        Board board = boardService.findBoardById(boardId);
        if (board == null) {
            return "redirect:/";
        }
        List<Board> boardsByLike = boardService.findTopByLikeCountAndFinalDate();
        List<Board> boardsByChat = boardService.findTopByChatCountAndFinalDate();

        model.addAttribute("boardsByLike", boardsByLike);
        model.addAttribute("boardsByChat", boardsByChat);

        // 세션에서 로그인 회원 정보를 가져옵니다.
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (model.containsAttribute("message")) {
            model.addAttribute("error", model.getAttribute("message"));
        }

        if (loginMember != null) {
            // 로그인한 회원 정보가 있다면 memberId를 모델에 추가
            model.addAttribute("loginMemberId", loginMember.getId());
        }

        if (loginMember != null) {
            boolean isLiked = likeService.isBoardLikedByMember(loginMember.getId(), boardId);
            model.addAttribute("isLiked", isLiked);
        }

        List<Comment> comments = board.getComments();
        model.addAttribute("comments", comments);

        List<String> formattedCommentDates = new ArrayList<>();
        for (Comment comment : comments) {
            formattedCommentDates.add(boardService.formatCommentDate(comment.getFinalDate()));
        }
        model.addAttribute("formattedCommentDates", formattedCommentDates);

        String formattedDate = boardService.formatFinalDate(board.getFinalDate());
        model.addAttribute("formattedDate", formattedDate);
        model.addAttribute("boardKind", board.getBoardKind());
        model.addAttribute("board", board);
        model.addAttribute("loggedIn", true);
        return "/board/reading/reading_normal";
    }

    // 게시글 목록 페이지를 보여줍니다.
    @GetMapping("/list")
    public String list(Model model) {
        List<Board> boards = boardService.findAllBoards();
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/all-paged-list")
    public String pagedList(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardPage = boardService.findBoardListWithPaging(pageable);
        model.addAttribute("boardPage", boardPage);
        return "board/pagedList";
    }

    @GetMapping("/paged-list")
    public String pagedList(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) BoardKind boardKind,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardPage;
        if(boardKind != null) {
            boardPage = boardService.findBoardListByKindWithPaging(boardKind, pageable);
        } else {
            boardPage = boardService.findBoardListWithPaging(pageable);
        }
        model.addAttribute("boardPage", boardPage);
        return "board/pagedList";
    }

    // 게시글 검색 처리를 합니다.
    @PostMapping("/search")
    public String search(@RequestParam String title, Model model) {
        List<Board> boards = boardService.searchBoardsByTitle(title);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    // 게시글 수정 폼 페이지를 보여줍니다.
    @GetMapping("/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        Board board = boardService.findBoardById(boardId);
        if (board == null) {
            return "redirect:/"; // 게시글이 존재하지 않으면 메인 페이지로 리다이렉트
        }
        BoardForm boardForm = new BoardForm();
        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        boardForm.setFinalDate(board.getFinalDate());
        model.addAttribute("boardForm", boardForm);
        return "board/editForm";
    }

    // 게시글 수정을 처리합니다.
    @PostMapping("/{boardId}/edit")
    public String edit(@PathVariable Long boardId,
                       @Valid @ModelAttribute BoardForm form,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/editForm";
        }
        Board updatedBoard = boardService.updateBoard(boardId, form.toBoard());
        if (updatedBoard == null) {
            return "redirect:/"; // 게시글이 존재하지 않으면 메인 페이지로 리다이렉트
        }
        return "redirect:/board/" + updatedBoard.getId();
    }

    //게시글 삭제를 처리합니다.
    @DeleteMapping("/delete/{boardId}")
    public String delete(@PathVariable Long boardId, HttpSession session, RedirectAttributes redirectAttributes) {
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요한 서비스입니다.");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }
        try {
            boardService.deleteBoard(boardId, loginMember);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/board/reading/" + boardId; // 삭제 실패 시 해당 게시글로 리다이렉트
        }
        return "redirect:/board/board_collection";
    }

//    // 게시글 삭제를 처리합니다.
//    @PostMapping("/delete/{boardId}")
//    public String delete(@PathVariable Long boardId) {
//        boardService.deleteBoard(boardId);
//        return "redirect:/board/board_collection";
//    }

    @GetMapping("/board_collection")
    public String collectionPage(Model model) {

        model.addAttribute("loggedIn", true);

        // 자유게시판의 최근 게시글 7개
        addRecentBoardsToModel(BoardKind.FREE, "recentFreeBoards", model);

        addRecentBoardsToModel(BoardKind.MARKET, "recentMarketBoards", model);
        addRecentBoardsToModel(BoardKind.FRESH, "recentFreshBoards", model);
        addRecentBoardsToModel(BoardKind.FOSSIL, "recentFossilBoards", model);
        addRecentBoardsToModel(BoardKind.INFO, "recentInfoBoards", model);
        addRecentBoardsToModel(BoardKind.CAREER, "recentCareerBoards", model);

        model.addAttribute("loggedIn", true);
        return "board/board_collection";
    }

    @GetMapping("/board_department_collection")
    public String collectionDepartmentPage(Model model,@ModelAttribute("loggedInMember") Member loggedInMember) {

        // 학과 정보가 없는 경우
        if (loggedInMember.getDepartment() == null || loggedInMember.getDepartment().isEmpty()) {
            // 인증 페이지로 리다이렉트
            return "redirect:/personal_info/certification";
        }
        model.addAttribute("loggedIn", true);

        addRecentDepartmentBoardsToModel(BoardKind.TIP, "recentTipBoards", model, loggedInMember);  // 여기에 loggedInMember 추가
        addRecentDepartmentBoardsToModel(BoardKind.REPORT, "recentReportBoards", model, loggedInMember);
        addRecentDepartmentBoardsToModel(BoardKind.QNA, "recentQnaBoards", model, loggedInMember);

        return "board/board_department_collection";
    }

    private void addRecentBoardsToModel(BoardKind boardKind, String attributeName, Model model) {
        List<MainBoardDTO> recentBoards = boardService.findRecentBoardsByKindForMainPage(boardKind, BoardConstants.RECENT_BOARD_LIMIT_SEVEN);
        model.addAttribute(attributeName, recentBoards);
    }

    private void addRecentDepartmentBoardsToModel(BoardKind boardKind, String attributeName, Model model, Member loggedInMember) {
        List<MainBoardDTO> recentBoards = boardService.findRecentBoardsByKindForDepartmentBoard(boardKind, BoardConstants.RECENT_BOARD_LIMIT_SEVEN, loggedInMember.getSchoolName(), loggedInMember.getDepartment());
        model.addAttribute(attributeName, recentBoards);
    }

    @GetMapping("/{boardKind}")
    public String showBoardSummary(@PathVariable BoardKind boardKind,
                                   @RequestParam(defaultValue = "0") int page,
                                   @ModelAttribute("loggedInMember") Member loggedInMember,
                                   Model model) {
        // 로그인 여부 확인
        if (loggedInMember == null) {
            // 로그인하지 않은 사용자를 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        // 학과 게시판에 해당하는 BoardKind들의 요청이 들어올 때, 학과 정보가 없는 사용자는 certification 페이지로 리다이렉트
        if (Arrays.asList(BoardKind.ISSUE, BoardKind.TIP, BoardKind.REPORT, BoardKind.QNA).contains(boardKind)) {
            if (loggedInMember.getDepartment() == null || loggedInMember.getDepartment().isEmpty()) {
                return "redirect:/personal_info/certification";
            }
        }

        // 인기 게시글 정보 가져오기
        List<Board> boardsByLike = boardService.findTopByLikeCountAndFinalDate();
        List<Board> boardsByChat = boardService.findTopByChatCountAndFinalDate();

        // 모델에 데이터 추가
        model.addAttribute("boardsByLike", boardsByLike);
        model.addAttribute("boardsByChat", boardsByChat);

        Pageable pageable = PageRequest.of(page, 6);

        // 로그인한 사용자의 학과와 학교 정보를 기반으로 게시판 요약 정보 가져오기
        Page<BoardSummaryDTO> boardSummaryPage = boardService.getBoardSummaryByKindAndMember(boardKind, pageable, loggedInMember);

        model.addAttribute("boardSummaryPage", boardSummaryPage);
        model.addAttribute("boardKind", boardKind);
        model.addAttribute("loggedIn", true);

        return "board/testBoardSummaryList";
    }

    @GetMapping("/notice")
    public String notice(Model model){
        model.addAttribute("loggedIn", true);
        return "board/notice";
    }
}
