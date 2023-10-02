package TeamProject.TeamProjectWeb.domain;

import TeamProject.TeamProjectWeb.dto.FIleDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter @Setter
@Table(name = "file_table")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id; // 개별 아이디
    private String fileName; // 파일 이름
    private String uploadFileName; // 클라이언트가 올린 파일 이름
    private String serverFileName; // 서버에 저장될 파일 이름 => uuid 줄거임

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL) // fetch=FetchType.LAZY : 지연 로딩으로 실시간 업로딩 되는 것을 막음
    @JoinColumn(name = "files") // 외래키 => 조인할 속성 이름
    //@JsonBackReference // 양방향 연관관계에서 역참조 엔티티의 정보를 직렬화하지 않도록 하기(순환 참조로 인한 무한루프 방지)
    private Board board; // ManyToOne 으로 한 이유는 파일 하나가 올라 갈 수도 여러 개 올라 갈 수도 있어서

    public File(String uploadFileName, String serverFileName) {
        this.uploadFileName = uploadFileName;
        this.serverFileName = serverFileName;
    }
    public File() { }


    //== 편의 메소드 ==//
    @Value("${file.dir}")
    private String fileDir; // 파일 경로
    public String getFullPath(String fileName){
        return fileDir + fileName; // 파일경로 + 파일이름 = fileFullName
    }
    public List<File> serverFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<File> serverFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){ // 파일이 존재하면
                serverFileResult.add(storeFile(multipartFile));
            }
        }
        return serverFileResult;
    }
    public File storeFile(MultipartFile multipartFile) throws IOException { //== 파일 저장 ==//
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFileName = multipartFile.getOriginalFilename(); // 클라이언트가 업로드한 파일명
        String serverFileName = createStoreFileName(originalFileName); // 서버에 저장할 파일명

        // storeFileName(경로)를 가지고 transferTo() 메소드를 이용해 저장함
        multipartFile.transferTo(new java.io.File(getFullPath(serverFileName)));

        return new File(originalFileName, serverFileName);
    }
    private String createStoreFileName(String originalFilename) { //== 서버에 저장할 파일명 만들기 ==//
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext; // uuid.확장자로 리턴함
    }
    private String extractExt(String originalFilename) { //== 확장자만 잘라냄 ==//
        int pos = originalFilename.lastIndexOf(".");

        return originalFilename.substring(pos + 1);
    }
}