package csvTestDataParser;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main extends JFrame {
    protected static final int WINDOW_WIDTH = 1200;
    protected static final int WINDOW_HEIGHT = 600;
    protected static final int ZERO = 0;

    //TODO: patternek beolvasása, mentése, rekolrok megjelenítése

    String[] columnNames;

    public Main() {
        this.setTitle("CSV Test Adat Parser");
        this.setLayout(null);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Path path = Paths.get("src/main/resources/BAV-Zalogtargy létrehozása.csv");
        CSVTestDataParser csvTestDataParser = new CSVTestDataParser();
        csvTestDataParser.readFile(path);


        JTextArea textArea = new JTextArea();
        //generatedCodeTextArea.setBounds(600, 100, 600, 200);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBounds(WINDOW_WIDTH / 4, ZERO, (int)(WINDOW_WIDTH * 0.75f), WINDOW_HEIGHT / 2);
        add(scroll);
        //add(generatedCodeTextArea);
        //generatedCodeTextArea.append("test");

        JButton button = new JButton("generate");
        button.setBounds(405, 300, 100, 50);
        button.addActionListener(l -> {
            String text = textArea.getText();
            String pattern = text;
            String pattern2 = text;
            Optional<List<List<String>>> records = csvTestDataParser.getRecords();
            if (records.isPresent()) {
                List<List<String>> recordList = records.get();
                for (int i = 0; i < recordList.size(); i++) {
                    for (int j = 0; j < recordList.get(i).size(); j++) {
                        String columname = columnNames[j].replaceAll("\\P{Print}", "");
                        String searchString = "[[" + columname + "]]";
                        if (pattern.contains(searchString)) { //unicode karakterek miatt
                            pattern2 = pattern2.replace(searchString, recordList.get(i).get(j));
                            System.out.println(pattern2);

                        }
                    }
                    text = text.concat("\n" + pattern2);
                    pattern2 = pattern;
                }
                textArea.setText(text);
             /*   for (List<String> line : recordList) {
                    for (String value : line) {
                        //text.matches()
                        System.out.println(value);
                    }
                    System.out.println("\n");
                }*/
            }
        });
        add(button);

        Optional<List<String>> titles = csvTestDataParser.getTitles();
        if (titles.isPresent()) {
            List<String> titlesList = titles.get();
            columnNames = new String[titlesList.size()];
            columnNames = titlesList.toArray(columnNames);
            JList list = new JList(columnNames);
            JScrollPane listScrollPlane = new JScrollPane(list);
            add(listScrollPlane);
            listScrollPlane.setBounds(ZERO, ZERO, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 2);
            list.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    JList list = (JList) evt.getSource();
                    if (evt.getClickCount() == 2) {
                        textArea.replaceSelection("[[" + list.getSelectedValue().toString().replaceAll("\\P{Print}", "") + "]]");
                        System.out.println(textArea.getText());
                    }
                }
            });
        }
        table(csvTestDataParser, titles);

        this.setVisible(true);
    }

    private void table(CSVTestDataParser csvTestDataParser, Optional<List<String>> titles) {
        Optional<List<List<String>>> records = csvTestDataParser.getRecords();
        if (records.isPresent()) {
            List<List<String>> recordsList = records.get();
            List<String> strings = titles.get();
            String[] columnNames = new String[strings.size()];
            columnNames = strings.toArray(columnNames);

            String[][] array = new String[recordsList.size()][];
            int j = 0;
            for (List<String> nestedList : recordsList) {
                array[j++] = nestedList.toArray(new String[nestedList.size()]);
            }

            JTable table = new JTable(array, columnNames);
            JScrollPane tableScrollPlane = new JScrollPane(table);
            add(tableScrollPlane);
            tableScrollPlane.setBounds(ZERO, WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        //new Main();

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(Main::createAndShowGUI);
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        ParserWindow frame = new ParserWindow("CSV Test Data Parser");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.initializeWindow();

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

}
