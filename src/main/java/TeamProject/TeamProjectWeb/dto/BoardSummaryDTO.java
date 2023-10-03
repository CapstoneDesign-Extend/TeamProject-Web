package TeamProject.TeamProjectWeb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class BoardSummaryDTO {
    private Long id;                // Unique ID of the board content
    private String title;          // Title of the board content
    private String content;        // Full content of the board post
    private LocalDateTime finalDate;  // Date of post creation or last update
    private String author;         // Author of the post (either an actual name or 'anonymous')
    private long likeCnt;          // Count of likes the post has received
    private long chatCnt;          // Count of comments the post has received


    // getters and setters...


    public BoardSummaryDTO(Long id, String title, String content, LocalDateTime finalDate, String author, long likeCnt, long chatCnt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.finalDate = finalDate;
        this.author = author;
        this.likeCnt = likeCnt;
        this.chatCnt = chatCnt;
    }
}




//    public String getContentSnippet() {
//        if(content.length() <= 100) return content;
//        return content.substring(0, 100); // 처음부터 100자까지 내용을 반환
//    }