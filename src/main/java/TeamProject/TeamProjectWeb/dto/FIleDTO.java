package TeamProject.TeamProjectWeb.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FIleDTO {
    private String fileName; // 파일 이름
    private List<MultipartFile> imageFiles;
    private MultipartFile attachFile;
    private Long boardId; // db seq로 가져옴



}
