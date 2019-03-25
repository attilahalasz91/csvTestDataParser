package csvTestDataParser;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class CSVTestDataParser implements TestDataParser {

    private List<String> titles;
    CSVReader reader;
    private List<List<String>> records;

    public CSVTestDataParser() {
        this.titles = new ArrayList<>();
        this.records = new ArrayList<>();
    }


    @Override
    public void readFile(Path filePath) {
        try {
            reader = new CSVReader(new FileReader(filePath.toString()));
            titles = asList(reader.readNext());
            String[] line;
            while ((line = reader.readNext()) != null) {
                List<String> record = asList(line);
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<List<String>> getTitles() {
        if (titles == null || titles.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(titles);
        }
    }

    @Override
    public Optional<List<List<String>>> getRecords() {
        if (records == null || records.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(records);
        }

    }
}
