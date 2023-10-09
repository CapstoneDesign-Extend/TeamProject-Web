package TeamProject.TeamProjectWeb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;                    // 개별 아이디
    private String fileName;            // 저장된 파일명
    private String originalFileName;    // 원본 파일명
    private String contentType;         // 파일 타입
    private long size; // 파일 크기

    // 관계 정의
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public FileEntity(String originalFileName, String fileName) {
        this.originalFileName = originalFileName;
        this.fileName = fileName;
    }

    public FileEntity() {
    }
}
