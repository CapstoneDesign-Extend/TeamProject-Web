package TeamProject.TeamProjectWeb.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class MainBoardDTO {
    private String title;
    private LocalDateTime finalDate;

    public String getFormattedFinalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        return finalDate.format(formatter);
    }

    public MainBoardDTO(String title, LocalDateTime finalDate) {
        this.title = title;
        this.finalDate = finalDate;
    }
}
