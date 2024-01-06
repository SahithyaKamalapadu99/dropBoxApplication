import org.example.model.FileMetadata;
import org.example.repository.FileRepository;
import org.example.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FileServiceTests {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    public FileServiceTests() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenSaveFile_thenVerifySaved() throws IOException {
        // Mock a MultipartFile
        InputStream mockInputStream = new ByteArrayInputStream("File content".getBytes());

        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("example.txt");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getInputStream()).thenReturn(mockInputStream);

        FileMetadata savedFileMetadata = new FileMetadata();
        savedFileMetadata.setFileName("example.txt");
        when(fileRepository.save(any(FileMetadata.class))).thenReturn(savedFileMetadata);

        FileMetadata savedFile = fileService.saveFile(multipartFile);

        verify(fileRepository, times(1)).save(any(FileMetadata.class));
        Assertions.assertEquals("example.txt", savedFile.getFileName());
    }

}
