package org.example.service;

import org.example.model.FileMetadata;
import org.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public FileMetadata uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileService.saveFile(file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) throws IOException {
        byte[] fileContent = fileService.getFile(id);
        if (fileContent != null) {
            return ResponseEntity.ok()
                    .body(fileContent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FileMetadata> updateFile(@PathVariable String id,
                                                   @RequestParam("file") MultipartFile file) {
        try {
            FileMetadata updatedMetadata = fileService.updateFile(id, file);
            return ResponseEntity.ok(updatedMetadata);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id) throws IOException {
        if (fileService.deleteFile(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<FileMetadata> listFiles() {
        return fileService.listFiles();
    }
}
