package TeamProject.TeamProjectWeb.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MainBoardDTO {
    private String title;
    private LocalDateTime finalDate;

    public MainBoardDTO(String title, LocalDateTime finalDate) {
        this.title = title;
        this.finalDate = finalDate;
    }
}
