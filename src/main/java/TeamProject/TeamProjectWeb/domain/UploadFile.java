package TeamProject.TeamProjectWeb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UploadFile { // 파일 정보
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uploadfile_id")
    private Long id; // 개별 아이디
    private String uploadFileName; // 클라이언트가 올린 파일 이름
    private String serverFileName; // 서버에 저장될 파일 이름 => uuid 줄거임
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public UploadFile(String uploadFileName, String serverFileName) {
        this.uploadFileName = uploadFileName;
        this.serverFileName = serverFileName;
    }

    public UploadFile() {
    }
}
