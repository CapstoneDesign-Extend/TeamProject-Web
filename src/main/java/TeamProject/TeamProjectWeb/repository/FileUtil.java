package TeamProject.TeamProjectWeb.repository;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil { // 파일 데이터를 읽어오는 클래스

    /**
     * 파일을 읽어와서 byte 배열로 변환하는 메소드
     *
     * @param filePath 읽어올 파일의 경로
     * @return 변환된 byte 배열
     * @throws IOException 파일 읽기 오류가 발생할 경우
     */
    public static byte[] readFileData(String filePath) throws IOException {
        File file = new File(filePath); // 파일 경로를 사용하여 File 객체를 생성

        // 파일 크기를 구합니다.
        long fileSize = file.length();

        byte[] buffer = new byte[1024];
        int bytesRead;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // 파일 데이터를 임시로 저장할 ByteArrayOutputStream을 생성

        try (FileInputStream inputStream = new FileInputStream(file)) { // 파일을 읽어옴
            // 파일에서 데이터를 읽어옴. 더 이상 읽을 데이터가 없을 때까지 반복함
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // 읽은 데이터를 ByteArrayOutputStream에 씀
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        // ByteArrayOutputStream에서 byte 배열을 얻어 반환함
        return outputStream.toByteArray();
    }

    public static String getFileName(String filePath) { // 파일 이름 추출
        File file = new File(filePath);
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }

    public static String getFileExtension(String filePath) { // 파일 확장자 추출
        File file = new File(filePath);
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

}
