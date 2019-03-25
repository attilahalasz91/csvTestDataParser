package csvTestDataParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.swing.JFileChooser.APPROVE_OPTION;

public class ParserWindow extends JFrame {

    private CSVTestDataParser csvTestDataParser;
    private String[] columnNames;

    private JList columnNameList;
    private JTextArea templateTextArea;
    private JTextArea generatedCodeTextArea;
    private JButton generateCodeButton;
    private JButton loadTemplateButton;
    private JButton saveTemplateButton;
    private JButton loadCSVFileButton;
    private JLabel listLabel;
    private JTable table;

    List<String> titles;
    private List<List<String>> records;

    public ParserWindow(String title) throws HeadlessException {
        super(title);
    }

    public void initializeWindow() {
        Path path = Paths.get("src/main/resources/userdata.csv");
        loadCSVData(path);

        addComponentsToPane(this.getContentPane());
        addComponentListeners();
    }

    private void loadCSVData(Path path) {
        csvTestDataParser = new CSVTestDataParser();
        csvTestDataParser.readFile(path);

        Optional<java.util.List<String>> optionalTitles = csvTestDataParser.getTitles();
        if (optionalTitles.isPresent()) {
            titles = optionalTitles.get();
            columnNames = new String[titles.size()];
            columnNames = titles.toArray(columnNames);
        }

        Optional<List<List<String>>> optionalRecords = csvTestDataParser.getRecords();
        if (optionalRecords.isPresent()) {
            records = optionalRecords.get();
        }

    }

    private void addComponentsToPane(Container pane) {
        pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        pane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = getGridBagConstraints();

        listLabel = new JLabel("Column Names");
        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(listLabel, constraints);

        listLabel = new JLabel("Text for Template");
        constraints.gridx = 1;
        constraints.gridy = 0;
        pane.add(listLabel, constraints);

        listLabel = new JLabel("Generated Code");
        constraints.gridx = 2;
        constraints.gridy = 0;
        pane.add(listLabel, constraints);

        listLabel = new JLabel("Commands");
        constraints.gridx = 3;
        constraints.gridy = 0;
        pane.add(listLabel, constraints);

        columnNameList = new JList();
        JScrollPane listScrollPlane = new JScrollPane(columnNameList);
        constraints.weightx = 0.3;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 4;
        pane.add(listScrollPlane, constraints);
        columnNameList.setListData(columnNames);

        templateTextArea = new JTextArea(20, 20);
        JScrollPane scroll = new JScrollPane(templateTextArea);
        constraints.weightx = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridheight = 4;
        pane.add(scroll, constraints);

        generatedCodeTextArea = new JTextArea(20, 20);
        JScrollPane scroll2 = new JScrollPane(generatedCodeTextArea);
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridheight = 4;
        pane.add(scroll2, constraints);

        constraints = getGridBagConstraints();

        table = new JTable();
        loadTableData(table);
        JScrollPane tableScrollPlane = new JScrollPane(table);
        constraints.ipadx = 1000;
        constraints.weightx = 1.0;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 4;
        add(tableScrollPlane, constraints);


        constraints = getGridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;
        loadTemplateButton = new JButton("Load Template");
        constraints.gridx = 3;
        constraints.gridy = 1;
        add(loadTemplateButton, constraints);
        saveTemplateButton = new JButton("Save Template");
        constraints.gridx = 3;
        constraints.gridy = 2;
        add(saveTemplateButton, constraints);
        loadCSVFileButton = new JButton("Load CSV");
        constraints.gridx = 3;
        constraints.gridy = 3;
        add(loadCSVFileButton, constraints);
        generateCodeButton = new JButton("Generate Code");
        constraints.gridx = 3;
        constraints.gridy = 4;
        add(generateCodeButton, constraints);


    }

    private void loadTableData(JTable table) {
        List<String> strings = titles;
        String[] columnNames = new String[strings.size()];
        columnNames = strings.toArray(columnNames);
        String[][] array = new String[records.size()][];
        int j = 0;
        for (List<String> nestedList : records) {
            array[j++] = nestedList.toArray(new String[nestedList.size()]);
        }
        table.setModel(new DefaultTableModel(array, columnNames));
    }

    private GridBagConstraints getGridBagConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        return constraints;
    }

    private void addComponentListeners() {
        columnNameList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList columnNameList = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    templateTextArea.replaceSelection(
                            "[[" + columnNameList.getSelectedValue().toString()
                                    .replaceAll("\\P{Print}", "") + "]]");
                }
            }
        });

        generateCodeButton.addActionListener(l -> {
            boolean areSelectedRows = false;
            List<Integer> selectedRows = Arrays.stream(table.getSelectedRows()).boxed().collect(Collectors.toList());
            if (table.getSelectedRowCount() != 0) {
                areSelectedRows = true;
            }

            generatedCodeTextArea.setText("");
            String text = "";
            String pattern = templateTextArea.getText();
            String patternToCode = pattern;
            for (int i = 0; i < records.size(); i++) {
                if (areSelectedRows && selectedRows.contains(i)) {
                    patternToCode = processTemplate(pattern, patternToCode, i);
                    text = text.concat(patternToCode); //"\n\n" +
                    patternToCode = pattern;

                } else if (!areSelectedRows) {
                    patternToCode = processTemplate(pattern, patternToCode, i);
                    text = text.concat(patternToCode);//"\n\n" +
                    patternToCode = pattern;
                }
            }
            generatedCodeTextArea.setText(text);
        });

        loadCSVFileButton.addActionListener(l -> {
            final JFileChooser fc = new JFileChooser();
            int returnValue = fc.showOpenDialog(ParserWindow.this);
            if (returnValue == APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                loadCSVData(file.toPath());
                loadTableData(table);
                columnNameList.setListData(columnNames);
            }
        });

        saveTemplateButton.addActionListener(l -> {
            final JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save Pattern");
            int returnValue = fc.showOpenDialog(ParserWindow.this);
            if (returnValue == APPROVE_OPTION) {
                File fileToSave = fc.getSelectedFile();
                try {
                    PrintWriter pw = new PrintWriter(fileToSave);
                    pw.println(templateTextArea.getText());
                    pw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        loadTemplateButton.addActionListener(l -> {
            final JFileChooser fc = new JFileChooser();
            int returnValue = fc.showOpenDialog(ParserWindow.this);
            if (returnValue == APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    byte[] encoded = Files.readAllBytes(file.toPath());
                    templateTextArea.setText(new String(encoded, StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String processTemplate(String pattern, String patternToCode, int i) {
        for (int j = 0; j < records.get(i).size(); j++) {
            String columname = columnNames[j].replaceAll("\\P{Print}", "");
            String searchString = "[[" + columname + "]]";
            if (pattern.contains(searchString)) { //unicode karakterek miatt
                patternToCode = patternToCode.replace(searchString, records.get(i).get(j));
                //System.out.println(patternToCode);

            }
            String searchString2 = "[N[" + columname + "]N]";
            if (pattern.contains(searchString2)) {
                patternToCode = patternToCode.replace(searchString2, normalizeUnicodeToASCII(records.get(i).get(j)));
            }
        }
        return patternToCode;
    }

    private static String normalizeUnicodeToASCII(String string) {
        return java.text.Normalizer.normalize(string, java.text.Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

}
