import org.example.DropboxApplication;
import org.example.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(FileController.class)
@ContextConfiguration(classes = DropboxApplication.class)
public class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    public void whenGetFile_thenExpectStatusOk() throws Exception {
        when(fileService.getFile("some-id")).thenReturn(new byte[0]);

        mockMvc.perform(get("/files/{id}", "some-id"))
                .andExpect(status().isOk());

        verify(fileService, times(1)).getFile("some-id");

    }
}
