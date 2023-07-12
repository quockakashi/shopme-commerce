package com.shopme.admin;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    static public void saveFile(String folderName, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadFolder = Paths.get(folderName);

        if(Files.notExists(uploadFolder)) {
            Files.createDirectories(uploadFolder);
        }

        try(var inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadFolder.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Can not save file " + fileName, e);
        }
    }
}
