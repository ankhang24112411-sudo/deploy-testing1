package com.tttn.webthitracnghiem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadStorageService {
    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Autowired
    private IFileService fileService;

    public File upload(MultipartFile multipartFile, String relativeFolder) {
        Path folder = folder(relativeFolder);
        return fileService.uploadFile(multipartFile, folder.toString());
    }

    public File folderFile(String relativeFolder) {
        File folder = folder(relativeFolder).toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public void deleteUploadedFile(String url, String defaultUrl, String relativeFolder) {
        if (url == null || url.equals(defaultUrl)) {
            return;
        }

        String fileName = url.substring(url.lastIndexOf("/") + 1);
        Path file = folder(relativeFolder).resolve(fileName).normalize();

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path folder(String relativeFolder) {
        return Paths.get(uploadDir, relativeFolder).toAbsolutePath().normalize();
    }
}
