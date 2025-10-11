package com.IlyaTr.movie_catalog.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${app.image.bucket}")
    private String baseDir;

    @SneakyThrows
    public String upload(MultipartFile image, String subDir){
        Path dirPath = Path.of(baseDir, subDir);
        Files.createDirectories(dirPath);

        if (!image.isEmpty()){
            String imageName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "_"
                               + UUID.randomUUID() + "_"
                               + image.getOriginalFilename();
            Path fullPath =dirPath.resolve(imageName);
            Files.copy(image.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
            return imageName;
        }
        return null;

    }

    @SneakyThrows
    public Resource load(String imageName, String subDir){
        Path loadFrom = Path.of(baseDir, subDir).resolve(imageName);
        return new UrlResource(loadFrom.toUri());
    }

}
