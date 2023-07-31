package TeamProject.TeamProjectWeb.controller.file;


import TeamProject.TeamProjectWeb.domain.File;
import TeamProject.TeamProjectWeb.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    // 파일 업로드 폼을 보여줍니다.
    @GetMapping("/upload")
    public String uploadForm() {
        return "file/uploadForm";
    }

    // 파일을 업로드하고 저장합니다.
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        // 업로드된 파일을 서비스를 통해 저장합니다.
        fileService.saveFile(file);
        return "redirect:/";
    }

    // 파일 다운로드 기능을 수행합니다.
    @GetMapping("/{fileId}/download")
    @ResponseBody
    public byte[] download(@PathVariable Long fileId) {
        // 파일 ID를 이용하여 파일 데이터를 가져옵니다.
        File file = fileService.findById(fileId);

        // 파일 데이터를 byte 배열로 반환합니다.
        return file.getFileData();
    }

    // 파일 삭제 기능을 수행합니다.
    @PostMapping("/{fileId}/delete")
    public String delete(@PathVariable Long fileId) {
        // 파일 ID를 이용하여 파일을 삭제합니다.
        fileService.deleteFileById(fileId);
        return "redirect:/";
    }
}
