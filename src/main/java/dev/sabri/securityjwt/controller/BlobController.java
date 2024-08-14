package dev.sabri.securityjwt.controller;


import com.sun.net.httpserver.Authenticator;
import dev.sabri.securityjwt.client.imagestorage.ImageStorageClient;
import dev.sabri.securityjwt.system.StatusCode;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.sabri.securityjwt.system.*;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/files1")
public class BlobController {
    private final ImageStorageClient imageStorageClient;

    public BlobController(ImageStorageClient imageStorageClient) {
        this.imageStorageClient = imageStorageClient;
    }

    @PostMapping("/upload")
    public Result uploadImage(@RequestParam String containerName, @RequestParam MultipartFile file) throws IOException {
        try(InputStream inputStream = file.getInputStream()) {
            String imageUrl = this.imageStorageClient.uploadImage(containerName,file.getOriginalFilename(),inputStream, file.getSize());
            return new Result(true, StatusCode.SUCCESS, "Upload Image Success", imageUrl);
        }

    }
}
