package TeamProject.TeamProjectWeb.controller.board;

import TeamProject.TeamProjectWeb.domain.Board;
import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성 폼 페이지를 보여줍니다.
    @GetMapping("/write")
    public String writeForm(@ModelAttribute("boardForm") BoardForm form) {
        return "board/writeForm";
    }

    // 게시글 작성을 처리합니다.
    @PostMapping("/write")
    public String write(@Valid @ModelAttribute BoardForm form,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/writeForm";
        }
        Board board = form.toBoard();
        boardService.createBoard(board);
        return "redirect:/";
    }

    // 게시글 상세 페이지를 보여줍니다.
    @GetMapping("/{boardId}")
    public String view(@PathVariable Long boardId, Model model) {
        Board board = boardService.findBoardById(boardId);
        if (board == null) {
            return "redirect:/"; // 게시글이 존재하지 않으면 메인 페이지로 리다이렉트
        }
        model.addAttribute("board", board);
        return "board/detail";
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

    // 게시글 삭제를 처리합니다.
    @PostMapping("/{boardId}/delete")
    public String delete(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return "redirect:/";
    }
}
