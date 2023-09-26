package TeamProject.TeamProjectWeb.controller;

import TeamProject.TeamProjectWeb.constants.BoardConstants;
import TeamProject.TeamProjectWeb.controller.login.SessionConst;
import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.domain.Member;
import TeamProject.TeamProjectWeb.dto.MainBoardDTO;
import TeamProject.TeamProjectWeb.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService; // 서비스 의존성 추가

    @GetMapping("/")
    public String homeLogin(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                            Model model) {

        // 세션에 회원 데이터가 없으면 main
        if (loginMember == null) {
            // loggedIn 값을 false로 모델에 추가
            model.addAttribute("loggedIn", false);
            return "main";
        }

        // 자유게시판의 최근 게시글 5개
        addRecentBoardsToModel(BoardKind.FREE, "recentFreeBoards", model);
        // 장터게시판의 최근 게시글 5개
        addRecentBoardsToModel(BoardKind.MARKET, "recentMarketBoards", model);

//        // 세션에 회원 데이터가 있으면 게시글 데이터를 가져와서 추가
//        List<MainBoardDTO> recentBoards = boardService.findRecentBoardsForMainPage(BoardConstants.RECENT_BOARD_LIMIT);
//        model.addAttribute("recentBoards", recentBoards);

        // 세션이 유지되면 loginMain으로 이동
        model.addAttribute("member", loginMember);
        // loggedIn 값을 true로 모델에 추가
        model.addAttribute("loggedIn", true);
        return "loginMain";
    }

    private void addRecentBoardsToModel(BoardKind boardKind, String attributeName, Model model) {
        List<MainBoardDTO> recentBoards = boardService.findRecentBoardsByKindForMainPage(boardKind, BoardConstants.RECENT_BOARD_LIMIT);
        model.addAttribute(attributeName, recentBoards);
    }
}



/*
/@GetMapping("/")
    public String home() {
        return "home";
    }


@GetMapping("/")//쿠키가 없어도 익셉션을 발생시키지 않음 (로그인 안해도 홈에 올수있어야하니까)
    public String homeLogin(@CookieValue(name="memberId", required = false) Long memberId, Model model) {
        if (memberId == null) {
            return "home";
        }
        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }



if (memberId == null) {
            return "home";
        }
        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";

        }
        */