package TeamProject.TeamProjectWeb.domain;

import TeamProject.TeamProjectWeb.domain.UploadFile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "images_id")
    private Long id;

    private String itemName; // 저장할 이름
    @OneToMany(mappedBy = "images", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UploadFile> imageFiles = new ArrayList<>(); // 파일 리스트
    @OneToOne
    private Board board;

    public Images(String originalFilename) {
        itemName = originalFilename;
    }

    public Images() {
    }

    @Override
    public String toString() {
        return "Images{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", imageFiles=" + imageFiles +
                '}';
    }
}
