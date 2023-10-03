package TeamProject.TeamProjectWeb.utils;

import TeamProject.TeamProjectWeb.domain.BoardKind;
import TeamProject.TeamProjectWeb.dto.BoardSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<BoardSummaryDTO> findSummaryByBoardKind(BoardKind boardKind, Pageable pageable);
}
