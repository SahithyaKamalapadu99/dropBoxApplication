package org.example.service;

import org.example.model.FileMetadata;
import org.example.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private static final Path STORAGE_LOCATION = Paths.get("path/to/storage");

    @Autowired
    private FileRepository fileRepository;

    public FileService() {
        try {
            Files.createDirectories(STORAGE_LOCATION);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public FileMetadata saveFile(MultipartFile file) throws IOException {
        String fileId = UUID.randomUUID().toString();
        Path destinationFile = STORAGE_LOCATION.resolve(
                        Paths.get(fileId))
                .normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setSize(file.getSize());
        metadata.setCreatedAt(LocalDateTime.now());

        return fileRepository.save(metadata);
    }

    public byte[] getFile(String id) throws IOException {
        FileMetadata metadata = fileRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IOException("File not found with id " + id));

        Path file = STORAGE_LOCATION.resolve(metadata.getFileName());
        return Files.readAllBytes(file);
    }

    public FileMetadata updateFile(String id, MultipartFile file) throws IOException {
        FileMetadata metadata = fileRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IOException("File not found with id " + id));

        // If file with same name should be replaced, delete the old file first
        Files.deleteIfExists(STORAGE_LOCATION.resolve(metadata.getFileName()));
        metadata.setFileName(file.getOriginalFilename());
        metadata.setSize(file.getSize());
        metadata.setCreatedAt(LocalDateTime.now());

        // Save the new file
        Path destinationFile = STORAGE_LOCATION.resolve(metadata.getFileName())
                .normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        return fileRepository.save(metadata);
    }

    public boolean deleteFile(String id) throws IOException {
        FileMetadata metadata = fileRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IOException("File not found with id " + id));

        fileRepository.deleteById(Long.valueOf(id));
        Files.deleteIfExists(STORAGE_LOCATION.resolve(metadata.getFileName()));
        return false;
    }

    public List<FileMetadata> listFiles() {
        return fileRepository.findAll();
    }
}
