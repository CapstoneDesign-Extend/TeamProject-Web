package TeamProject.TeamProjectWeb.dto;

import TeamProject.TeamProjectWeb.domain.Images;
import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName; // 클라이언트가 올린 파일 이름
    private String storeFileName; // 서버에 저장될 파일 이름

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
