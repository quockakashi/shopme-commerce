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

    static public void cleanPath(String path) {
        Path dirPath = Paths.get(path);

        try(var list = Files.list(dirPath)){
            list.forEach(file -> {
                    if(!Files.isDirectory(file)) {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            System.out.println("Can't delete the file");
                        }
                    }
            });
        } catch (IOException e) {
            System.out.println("Can't list the directory");
        }
    }

    public static void removeDirectory(String dirName) throws IOException {
        cleanPath(dirName);
        Path dirPath = Paths.get(dirName);

        if(Files.isDirectory(dirPath)) {
            Files.delete(dirPath);
        }
    }
}
