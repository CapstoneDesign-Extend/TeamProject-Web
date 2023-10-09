package TeamProject.TeamProjectWeb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileDTO {
    private Long id;  // 파일 id
    private String fileName; // 파일 이름
    private String origenalFileName;  // 원본 파일명
    private String contentType;  // 파일 타입
    private Long size;  // 파일 크기
    private long boardId;  // 연관된 게시글의 id


}
