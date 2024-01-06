import org.example.DropboxApplication;
import org.example.model.FileMetadata;
import org.example.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = DropboxApplication.class)

public class FileRepositoryTests {

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void whenSaveFile_thenFindById() {
        FileMetadata file = new FileMetadata();
        file.setFileName("example.txt");
        file.setSize(234);

        file = fileRepository.save(file);
        FileMetadata found = fileRepository.findById(file.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getFileName()).isEqualTo(file.getFileName());
        assertThat(found.getSize()).isEqualTo(file.getSize());
    }
}
