package dev.sabri.securityjwt.service;


import dev.sabri.securityjwt.entity.File;
import dev.sabri.securityjwt.repo.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public File storeFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        File dbFile = new File();
        dbFile.setName(fileName);
        dbFile.setType(file.getContentType());
        dbFile.setData(file.getBytes());

        return fileRepository.save(dbFile);
    }

    public Optional<File> getFile(Long fileId) {
        return fileRepository.findById(fileId);
    }
}
