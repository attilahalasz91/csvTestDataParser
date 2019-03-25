package csvTestDataParser;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class CSVTestDataParserMain {

    static Set<Set<String>> allKeys = new HashSet<>();
    static Set<String> keys = new HashSet<>();
    static Set<String> tetelKeys = new HashSet<>();
    static Set<String> finomsagKeys = new HashSet<>();
    static Set<String> tipusKeys = new HashSet<>();
    static Set<String> becslesKeys = new HashSet<>();
    static Set<String> becsusKeys = new HashSet<>();
    static Set<String> arfolyamKeys = new HashSet<>();
    static Set<String> holvanKeys = new HashSet<>();
    static Set<String> anyagKeys = new HashSet<>();
    static Set<String> zalognemKeys = new HashSet<>();
    static boolean demofound = false;

    private static final String map = "Map";

    public static void main(String[] args) {
        String csvFile = "src/main/resources/BAV-Zalogtargy létrehozása.csv";
        //String csvFile = "src/main/resources/BAV-Ugyfel létrehozása.csv";
        String name = "";

        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(csvFile));
            FileWriter fileWriter = new FileWriter("src/main/resources/generatedCode.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            String[] line;
            reader.readNext();

            while ((line = reader.readNext()) != null) {

                generateZalogtargyak(printWriter, line);
                //generateUgyfelek(printWriter, line);

            }

            /*for (String key : keys) {
                printWriter.printf(name.concat(map).concat(".put(\"").concat(key).concat("\", );\n"));
            }*/
            allKeys.add(tetelKeys);
            allKeys.add(finomsagKeys);
            allKeys.add(tipusKeys);
            allKeys.add(becslesKeys);
            allKeys.add(becsusKeys);
            allKeys.add(arfolyamKeys);
            allKeys.add(holvanKeys);
            allKeys.add(anyagKeys);
            allKeys.add(zalognemKeys);
            for (Set<String> allKey : allKeys) {

                for (String key : allKey) {
                    printWriter.printf(name.concat(map).concat(".put(\"").concat(key).concat("\", );\n"));
                }
            }

            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished");
    }

    private static void generateUgyfelek(PrintWriter printWriter, String[] line) {
        printWriter.printf(
                "ugyfel(\"%s\", \"%s\", \"%s\", \"%s\", dateUgyfelhez(\"%s\"), \"%s\",\n" +
                        "          \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\",\n" +
                        "          kozteruletTipusaMap.get(\"%s\"), \"%s\", %s, \"%s\",\n" +
                        "          \"%s\", \"%s\", \"%s\",\"%s\",okmanyTipusMap.get(\"%s\"),\n" +
                        "          dateUgyfelhez(\"%s\"));\n\n",
                line[0], line[1], line[2], line[3], line[5], line[6], line[7], line[8], line[9], line[10],
                line[11], line[12], line[13], line[14], line[15], line[17], line[18], line[19], line[20], line[21], line[22], line[23]
        );
        keys.add(line[13]);
        keys.add(line[22]);
    }

    private static void generateZalogtargyak(PrintWriter printWriter, String[] line) {
        if (line[18].equals("DEMO")) {
            printWriter.printf(
                /*"zalogtargyMap.put(\"%s\", new TestZalogtargyBuilder()\n" +
                        "\t\t\t\t\t.azon(\"%s\")\n" +
                        "\t\t\t\t\t.becslesIdopont(dateZalogtargyhoz(\"%s\"))\n" +
                        "\t\t\t\t\t.bruttoSulyG(%sf)\n" +
                        "                    .nettoSulyG(%sf)\n" +
                        "                    .szelessegMm(%sf)\n" +
                        "                    .hosszMm(%sf)\n" +
                        "                    .tetel(raktariTetelek.get(\"%s\"))\n" +
                        "                    .finomsag(finomsagMap.get(\"%s%sFinomsag\"))\n" +
                        "                    .tipus(tipusMap.get(\"%s\"))\n" +
                        "                    .becsles(becslesMap.get(\"%s\"))\n" +
                        "                    .leiras(\"%s\")\n" +
                        "                    .megjegyzes(\"%s\")\n" +
                        "                    .becsultErtek(%sf)\n" +
                        "                    .becsus(becsus.get(\"%s\"))\n" +
                        "                    .arfolyam(arfolyam.get(\"%s%sArfolyam\"))\n" +
                        "                    .holVan(holVanMap.get(\"%s\"))\n" +
                        "                    .anyag(anyagMap.get(\"%s\"))\n" +
                        "                    .zalognem(zalognemMap.get(\"%s\"))\n" +
                        "                    .create());\n\n",*/
                    "zalogtargyMap.put(\"%s\", new TestZalogtargyBuilder()\n" +
                            "\t\t\t\t\t.azon(\"%s\")\n" +
                            "\t\t\t\t\t.becslesIdopont(dateZalogtargyhoz(\"%s\"))\n" +
                            "\t\t\t\t\t.bruttoSulyG(%sf)\n" +
                            "                    .nettoSulyG(%sf)\n" +
                            "                    .szelessegMm(%sf)\n" +
                            "                    .hosszMm(%sf)\n" +
                            "                    .tetel(raktariTetelek.get(\"%s\"))\n" +
                            "                    .finomsag(%s%sfinomsag)\n" +
                            "                    .tipus(%s)\n" +
                            "                    .becsles(%s)\n" +
                            "                    .leiras(\"%s\")\n" +
                            "                    .megjegyzes(\"%s\")\n" +
                            "                    .becsultErtek(%sf)\n" +
                            "                    .becsus(becsusok.get(\"%s\"))\n" +
                            "                    .arfolyam(%s%sarfolyam)\n" +
                            "                    .holVan(%s)\n" +
                            "                    .anyag(AnyagEnum.%s)\n" +
                            "                    .zalognem(%s)\n" +
                            "                    .create());\n\n",
                    line[0], line[0], line[1], line[2], line[3], line[4], line[5], line[6], normalizeUnicodeToASCII(line[16]), line[7], normalizeUnicodeToASCII(line[8]), normalizeUnicodeToASCII(line[9]),
                    line[10], line[11], line[12], line[13], normalizeUnicodeToASCII(line[16]), line[14],
                    normalizeUnicodeToASCII(line[15].toLowerCase()).split(" ")[0],
                    normalizeUnicodeToASCII(line[16]), normalizeUnicodeToASCII(line[17].toLowerCase())
            );

           /* if (!demofound) {
                if (line[18].equals("DEMO")) {
                    demofound = true;
                    printWriter.printf("\n--------------------------------------demo adatok kezdete-------------------------------------------\n\n");
                }
            }*/


            tetelKeys.add(line[6]);
            finomsagKeys.add(line[16].concat(line[7]).concat("Finomsag"));
            tipusKeys.add(line[8]);
            becslesKeys.add(line[9]);
            becsusKeys.add(line[13]);
            arfolyamKeys.add(line[16].concat(line[14]).concat("Arfolyam"));
            holvanKeys.add(line[15]);
            anyagKeys.add(line[16]);
            zalognemKeys.add(line[17]);
        }
    }

    private static String normalizeUnicodeToASCII(String string){
        return java.text.Normalizer.normalize(string, java.text.Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","");
    }

    public void generateCodeToTxt() throws IOException {


    }
}
