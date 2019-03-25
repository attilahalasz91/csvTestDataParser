package csvTestDataParser;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface TestDataParser {

    void readFile(Path filePath);

    Optional<List<String>> getTitles();

    Optional<List<List<String>>> getRecords();
}
