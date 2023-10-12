package TeamProject.TeamProjectWeb.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uploadFile_id")
    private Long id;
    private String uploadFileName; // 클라이언트가 올린 파일 이름
    private String storeFileName; // 서버에 저장될 파일 이름
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "images_id")
    private Images images;


    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    public UploadFile() {

    }
}
