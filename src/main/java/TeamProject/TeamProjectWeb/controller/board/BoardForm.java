package TeamProject.TeamProjectWeb.controller.board;

import lombok.Data;
import TeamProject.TeamProjectWeb.domain.Board;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime; // java.time 패키지 임포트

@Data
public class BoardForm {

    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    @NotEmpty(message = "최종 수정일을 입력해주세요.")
    private LocalDateTime finalDate; // finalDate 필드를 LocalDateTime으로 변경

    public Board toBoard() {
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setFinalDate(finalDate); // finalDate를 LocalDateTime으로 설정
        return board;
    }
}

/*에러 메시지를 보면 setFinalDate(java.time.LocalDateTime) 메서드를
(java.lang.@jakarta.validation.constraints.NotEmpty(message = "마감일을 입력해주세요.") String) 타입에 적용할 수 없다고 나와 있습니다.
이 에러는 setFinalDate 메서드의 파라미터 타입과 @NotEmpty 어노테이션의 타입이 맞지 않아서 발생하는 것입니다.

@NotEmpty 어노테이션은 문자열 혹은 컬렉션 타입에만 사용할 수 있습니다. 하지만 setFinalDate 메서드의 파라미터는 java.time.LocalDateTime 타입입니다.
@NotEmpty 어노테이션은 해당 필드가 빈 값이 아니어야 함을 검증하는 것이기 때문에 LocalDateTime 타입과는 사용할 수 없습니다.

setFinalDate 메서드의 파라미터를 String 타입으로 변경해주시면 해결될 것입니다. 그리고 finalDate 필드의 타입을 LocalDateTime으로 변경하는 것도 고려해볼 수 있습니다.
이렇게 하면 문자열과 LocalDateTime 사이의 변환 작업이 필요 없어지고, 확장성과 유지보수성이 좋아질 수 있습니다.*/