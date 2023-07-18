package TeamProject.TeamProjectWeb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
@Table(name = "file_table")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id; // 개별 아이디

    private String fileName; // 파일 이름

    private long fileSize; // 파일 사이즈

    private String fileType; // 파일 타입

    @Lob // 대용량 데이터 매핑 시 필요 ex) BOLB 형식
    private byte[] fileData; // 파일에 대한 데이터로 파일의 내용을 바이트 배열로 변환한 데이터가 저장됨

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL) // fetch=FetchType.LAZY : 지연 로딩으로 실시간 업로딩 되는 것을 막음
    @JoinColumn(name = "files") // 외래키 => 조인할 속성 이름
    private Board board;


}